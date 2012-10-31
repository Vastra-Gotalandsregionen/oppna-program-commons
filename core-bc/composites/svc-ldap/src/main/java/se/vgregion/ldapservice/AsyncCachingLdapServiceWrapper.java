package se.vgregion.ldapservice;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

/**
 * This implementation wraps an {@link LdapService} and makes the calls asynchronously (when possible), thus fetches the
 * result lazily. The returned object is a wrapper (when possible) implementation which uses concurrency to enable lazy
 * loading.
 *
 * @author Patrik Bergström
 * @see LdapService
 */
@Service
@SuppressWarnings("unchecked")
public class AsyncCachingLdapServiceWrapper implements LdapService {

    private static final CacheManager SINGLE_CACHE_MANAGER = CacheManager.create();

    private Ehcache cache;
    private LdapService ldapService;
    private static final int N_THREADS = 10;
    private ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> lastScheduledTask;

    /**
     * Constructor.
     *
     * @param ldapService ldapService
     */
    public AsyncCachingLdapServiceWrapper(LdapService ldapService) {
        final int hours = 48;
        final int minutes = 60;
        final int seconds = 60;
        final int millis = 1000;
        long timeoutInMillis = hours * minutes * seconds * millis; // 48 hours
        // The timeout arguments mean that it's only the time from creation that matters; the idle time can
        // never be longer than the time since creation.
        String name = this.getClass() + "Cache_" + timeoutInMillis;
        if (!SINGLE_CACHE_MANAGER.cacheExists(name)) {
            final int maxElementsInMemory = 500;
            this.cache = new Cache(name, maxElementsInMemory, false, false, timeoutInMillis, timeoutInMillis);
            SINGLE_CACHE_MANAGER.addCache(cache);
        } else {
            this.cache = SINGLE_CACHE_MANAGER.getCache(name);
        }

        this.ldapService = ldapService;
    }

    /**
     * Constructor.
     *
     * @param ldapService       ldapService
     * @param timeToLiveSeconds the time the cached elements should live (from creation)
     */
    public AsyncCachingLdapServiceWrapper(LdapService ldapService, long timeToLiveSeconds) {
        // The timeout arguments mean that it's only the time from creation that matters; the idle time can
        // never be longer than the time since creation.
        String name = this.getClass() + "Cache_" + timeToLiveSeconds;
        if (!SINGLE_CACHE_MANAGER.cacheExists(name)) {
            final int maxElementsInMemory = 500;
            cache = new Cache(name, maxElementsInMemory, false, false, timeToLiveSeconds, timeToLiveSeconds);
            SINGLE_CACHE_MANAGER.addCache(cache);
        } else {
            this.cache = SINGLE_CACHE_MANAGER.getCache(name);
        }

        this.ldapService = ldapService;
    }

    @Override
    public LdapUser[] search(final String base, final String filter) {
        Integer cacheKey = createCacheKey(base, filter);
        Element element = cache.get(cacheKey);
        if (element != null) {
            return (LdapUser[]) element.getValue();
        }

        // We cannot make a wrapper of an Array, so just delegate synchronously.
        LdapUser[] search = ldapService.search(base, filter);

        if (search != null) {
            cache.put(new Element(cacheKey, search));
        }

        return search;
    }

    private Integer createCacheKey(Object... args) {
        int callingLineNumber = new Exception().getStackTrace()[1].getLineNumber();
        final int prime = 7;
        int hash = callingLineNumber * prime;
        for (Object arg : args) {
            if (arg != null) {
                hash += arg.hashCode() * prime;
            }
        }

        return hash;
    }

    @Override
    public LdapUser[] search(String s, String s1, String[] strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LdapUser getLdapUser(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LdapUser getLdapUser(String s, String s1, String[] strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addLdapUser(String s, HashMap<String, String> stringStringHashMap) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean modifyLdapUser(LdapUser ldapUser, HashMap<String, String> stringStringHashMap) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteLdapUser(LdapUser ldapUser) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LdapUser getLdapUserByUid(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LdapUser getLdapUserByUid(final String userId) {
        Integer cacheKey = createCacheKey(userId);
        Element element = cache.get(cacheKey);
        if (element != null) {
            return (LdapUser) element.getValue();
        }

        Callable callable = new Callable<LdapUser>() {
            @Override
            public LdapUser call() throws Exception {
                return ldapService.getLdapUserByUid(userId);
            }
        };

        Future<LdapUser> futureLdapUser = executor.submit(callable);

        AsyncLdapUserWrapper ldapUser = new AsyncLdapUserWrapper(futureLdapUser, cacheKey);

        cache.put(new Element(cacheKey, ldapUser));

        // Cleanup null objects since we don't want to cache them.
        if (lastScheduledTask == null || lastScheduledTask.isDone()) {
            // Otherwise it is unnecessary to pile up tasks
            final int delay = 5;
            lastScheduledTask = scheduledExecutorService.schedule(new Runnable() {
                @Override
                public void run() {
                    final List keys = cache.getKeys();
                    for (Object key : keys) {
                        final Element element1 = cache.get(key);
                        if (element1 == null || element1.getObjectValue() == null) {
                            cache.remove(key);
                        }
                    }
                }
            }, delay, TimeUnit.SECONDS);
        }

        return ldapUser;
    }

    private class AsyncLdapUserWrapper implements LdapUser {
        private final Logger LOGGER = LoggerFactory.getLogger(AsyncLdapUserWrapper.class);
        private static final long serialVersionUID = -1123850060733039675L;

        private Future<LdapUser> futureLdapUser;
        private Integer cacheKey;

        /**
         * Constructor.
         *
         * @param futureLdapUser futureLdapUser
         * @param cacheKey
         */
        public AsyncLdapUserWrapper(Future<LdapUser> futureLdapUser, Integer cacheKey) {
            this.futureLdapUser = futureLdapUser;
            this.cacheKey = cacheKey;
        }

        @Override
        public String getDn() {
            try {
                LdapUser ldapUser = futureLdapUser.get();
                if (ldapUser == null) {
                    return null;
                }
                cache.put(new Element(cacheKey, ldapUser));
                return ldapUser.getDn();
            } catch (InterruptedException e) {
                throw new LdapUserRetrievalException(e);
            } catch (ExecutionException e) {
                throw new LdapUserRetrievalException(e);
            }
        }

        @Override
        public String getAttributeValue(String s) {
            try {
                LdapUser ldapUser = futureLdapUser.get();
                if (ldapUser == null) {
                    return null;
                }
                cache.put(new Element(cacheKey, ldapUser));
                return ldapUser.getAttributeValue(s);
            } catch (InterruptedException e) {
                throw new LdapUserRetrievalException(e);
            } catch (ExecutionException e) {
                throw new LdapUserRetrievalException(e);
            }
        }

        @Override
        public String[] getAttributeValues(String s) {
            try {
                LdapUser ldapUser = futureLdapUser.get();
                if (ldapUser == null) {
                    return null;
                }
                cache.put(new Element(cacheKey, ldapUser));
                return ldapUser.getAttributeValues(s);
            } catch (InterruptedException e) {
                throw new LdapUserRetrievalException(e);
            } catch (ExecutionException e) {
                throw new LdapUserRetrievalException(e);
            }
        }

        @Override
        public Map<String, ArrayList<String>> getAttributes() {
            try {
                LdapUser ldapUser = futureLdapUser.get();
                if (ldapUser == null) {
                    return null;
                }
                cache.put(new Element(cacheKey, ldapUser));
                return ldapUser.getAttributes();
            } catch (InterruptedException e) {
                throw new LdapUserRetrievalException(e);
            } catch (ExecutionException e) {
                throw new LdapUserRetrievalException(e);
            }
        }

        @Override
        public void clearAttribute(String s) {
            try {
                LdapUser ldapUser = futureLdapUser.get();
                if (ldapUser == null) {
                    return;
                }
                cache.put(new Element(cacheKey, ldapUser));
                ldapUser.clearAttribute(s);
            } catch (InterruptedException e) {
                throw new LdapUserRetrievalException(e);
            } catch (ExecutionException e) {
                throw new LdapUserRetrievalException(e);
            }
        }

        @Override
        public void setAttributeValue(String s, Object o) {
            try {
                LdapUser ldapUser = futureLdapUser.get();
                if (ldapUser == null) {
                    return;
                }
                cache.put(new Element(cacheKey, ldapUser));
                ldapUser.setAttributeValue(s, o);
            } catch (InterruptedException e) {
                throw new LdapUserRetrievalException(e);
            } catch (ExecutionException e) {
                throw new LdapUserRetrievalException(e);
            }
        }

        @Override
        public void addAttributeValue(String s, Object o) {
            try {
                LdapUser ldapUser = futureLdapUser.get();
                if (ldapUser == null) {
                    return;
                }
                cache.put(new Element(cacheKey, ldapUser));
                ldapUser.addAttributeValue(s, o);
            } catch (InterruptedException e) {
                throw new LdapUserRetrievalException(e);
            } catch (ExecutionException e) {
                throw new LdapUserRetrievalException(e);
            }
        }

        @Override
        public void setAttributeValue(String s, Object[] objects) {
            try {
                LdapUser ldapUser = futureLdapUser.get();
                if (ldapUser == null) {
                    return;
                }
                cache.put(new Element(cacheKey, ldapUser));
                ldapUser.setAttributeValue(s, objects);
            } catch (InterruptedException e) {
                throw new LdapUserRetrievalException(e);
            } catch (ExecutionException e) {
                throw new LdapUserRetrievalException(e);
            }
        }
    }
}
