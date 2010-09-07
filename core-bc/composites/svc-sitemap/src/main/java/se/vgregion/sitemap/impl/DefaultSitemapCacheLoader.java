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

import se.vgregion.sitemap.CacheLoader;
import se.vgregion.sitemap.SitemapEntryLoader;
import se.vgregion.sitemap.model.SitemapCache;

/**
 * Implementation of the CacheLoader interface which populates a SitemapCache by using the appropriate loader.
 */
public abstract class DefaultSitemapCacheLoader implements CacheLoader<SitemapCache> {

    private final SitemapEntryLoader sitemapEntryLoader;
    private final String applicationBaseURL;

    /**
     * Constructs a new {@link DefaultSitemapCacheLoader}.
     * 
     * @param webbisCacheService
     *            The {@link WebbisCacheServiceImpl} implementation to use to fetch units.
     * @param applicationBaseURL
     *            The external URL to the application.
     */
    public DefaultSitemapCacheLoader(final SitemapEntryLoader sitemapEntryLoader, String applicationBaseURL) {
        this.sitemapEntryLoader = sitemapEntryLoader;
        this.applicationBaseURL = applicationBaseURL;
    }

    /**
     * {@inheritDoc}
     */
    public SitemapCache createEmptyCache() {
        return new SitemapCache();
    }

    public SitemapCache loadCache() {
        SitemapCache cache = new SitemapCache();

        populateSitemapEntryCache(cache);

        return cache;
    }

    public SitemapEntryLoader getSitemapEntryLoader() {
        return sitemapEntryLoader;
    }

    public String getApplicationBaseURL() {
        return applicationBaseURL;
    }

    public abstract void populateSitemapEntryCache(SitemapCache cache);
}
