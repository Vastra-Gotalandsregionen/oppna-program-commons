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

/**
 * 
 */
package se.vgregion.sitemap.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.vgregion.sitemap.CacheLoader;
import se.vgregion.sitemap.model.SitemapCache;

/**
 * @author david.rosell@redpill-linpro.com
 * 
 */
public class DefaultCacheServiceTest {

    private DefaultCacheService<MockCache> cacheService;
    private CacheLoader<MockCache> cacheLoader;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        cacheLoader = new CacheLoader<DefaultCacheServiceTest.MockCache>() {
            MockCache mockCache;

            @Override
            public MockCache loadCache() {
                List<String> list = new ArrayList<String>();
                list.add("apa");
                list.add("bepa");
                list.add("cepa");
                mockCache.setList(list);

                return mockCache;
            }

            @Override
            public MockCache createEmptyCache() {
                mockCache = new MockCache();
                mockCache.setList(new ArrayList<String>());
                return mockCache;
            }

            @Override
            public void populateSitemapEntryCache(SitemapCache cache) {
                // do nothing
            }
        };

        cacheService = new DefaultCacheService<MockCache>(cacheLoader);
    }

    @Test
    public final void initToValidState() {
        assertEquals(MockCache.class, cacheService.getCache().getClass());
    }

    @Test
    public final void testReloadCache() {
        // before loaded
        assertEquals(0, cacheService.getCache().getList().size());

        // after loaded
        cacheService.reloadCache();
        MockCache mockCache = cacheService.getCache();
        assertEquals(3, mockCache.getList().size());

        // after explicit change
        mockCache.getList().add("depa");
        mockCache = cacheService.getCache();
        assertEquals(4, mockCache.getList().size());

        cacheService.reloadCache();
        assertEquals(3, mockCache.getList().size());
    }

    private class MockCache {
        private List<String> list;

        void setList(List<String> list) {
            this.list = list;
        }

        List<String> getList() {
            return list;
        }
    }
}
