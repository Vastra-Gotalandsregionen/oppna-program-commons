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

package se.vgregion.sitemap.service;

import java.util.List;

import se.vgregion.sitemap.SitemapGenerator;
import se.vgregion.sitemap.impl.DefaultCacheService;
import se.vgregion.sitemap.model.SitemapCache;
import se.vgregion.sitemap.model.SitemapEntry;

/**
 * Supporting bean for sitemap-servlets.
 */
public class SitemapService<T> {
    private final DefaultCacheService<T> sitemapCacheService;
    private final SitemapGenerator sitemapGenerator;

    /**
     * Constructs a new {@link SitemapService}.
     * 
     * @param sitemapGenerator
     *            The SitemapGenerator implementation to use to generate the sitemap XML.
     * @param sitemapCacheService
     *            The {@link SitemapCacheServiceImpl} to get the entries for the sitemap from.
     */
    public SitemapService(SitemapGenerator sitemapGenerator, DefaultCacheService<T> sitemapCacheService) {
        this.sitemapCacheService = sitemapCacheService;
        this.sitemapGenerator = sitemapGenerator;
    }

    /**
     * Retrieves the content for the sitemap based on the units from the cache. The values of the persons and units
     * parameters decide which information should be generated. If none or both are set to true, all information
     * regarding both persons and units are generated, otherwise only the selected kind of information is
     * generated.
     * 
     * @return The content for the sitemap.
     */
    public String getSitemapContent() {

        SitemapCache cache = (SitemapCache) sitemapCacheService.getCache();

        List<SitemapEntry> entries = cache.getEntries();
        // Check if list of entries is populated, otherwise we fill it up!
        if (entries.size() < 1) {
            sitemapCacheService.reloadCache();
            cache = (SitemapCache) sitemapCacheService.getCache();
            entries = cache.getEntries();
        }

        return sitemapGenerator.generate(entries);
    }
}
