/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.sitemap.impl;

import java.util.concurrent.atomic.AtomicReference;

import se.vgregion.sitemap.CacheLoader;

/**
 * Cache service for various information caching.
 * 
 * @param <T>
 *            Type of cache for this service instance.
 */
public class DefaultCacheService<T> {
    private final CacheLoader<T> cacheLoader;
    private final AtomicReference<T> cache = new AtomicReference<T>();

    /**
     * Constructs a new CacheService.
     * 
     * @param cacheLoader
     *            The loader for this service instance.
     */
    public DefaultCacheService(CacheLoader<T> cacheLoader) {
        this.cacheLoader = cacheLoader;
        cache.set(cacheLoader.createEmptyCache());
    }

    /**
     * Reloads the cache from DB using the cache loader instance.
     */
    public void reloadCache() {
        cache.set(cacheLoader.loadCache());
    }

    public T getCache() {
        return cache.get();
    }
}
