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

import java.util.Date;

import se.vgregion.sitemap.SitemapEntryLoader;
import se.vgregion.sitemap.impl.DefaultSitemapCacheLoader;
import se.vgregion.sitemap.model.SitemapCache;
import se.vgregion.sitemap.model.SitemapEntry;
import se.vgregion.sitemap.util.W3CDateTimeUtil;

public class MockSitemapCacheLoader extends DefaultSitemapCacheLoader {

    public MockSitemapCacheLoader(SitemapEntryLoader sitemapEntryLoader, String applicationBaseURL) {
        super(sitemapEntryLoader, applicationBaseURL);
    }

    @Override
    public void populateSitemapEntryCache(SitemapCache cache) {
        Date lastModifiedDate = new Date();
        Date createdDate = new Date();

        String lastmod = W3CDateTimeUtil.getLastModifiedW3CDateTime(lastModifiedDate, createdDate);
        SitemapEntry entry = new SitemapEntry(getApplicationBaseURL() + "?mockId=1", lastmod, "daily");
        cache.add(entry);

        lastmod = W3CDateTimeUtil.getLastModifiedW3CDateTime(null, createdDate);
        entry = new SitemapEntry(getApplicationBaseURL() + "?mockId=2", lastmod, "daily");
        cache.add(entry);
    }
}
