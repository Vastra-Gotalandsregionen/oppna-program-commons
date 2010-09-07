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

package se.vgregion.sitemap;

import se.vgregion.sitemap.model.SitemapCache;

/**
 * Loader for a cache.
 * 
 * @param <T>
 *            The type of cache to load.
 */
public interface CacheLoader<T> {
    /**
     * Loads the cache from the LDAP directory.
     * 
     * @return A fully populated cache instance.
     */
    T loadCache();

    /**
     * Creates an empty cache.
     * 
     * @return An empty cache.
     */
    T createEmptyCache();

    /**
     * Populate cache with sitemap entries.
     * 
     * @param cache
     */
    void populateSitemapEntryCache(SitemapCache cache);
}
