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
package se.vgregion.sitemap.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.vgregion.sitemap.impl.DefaultCacheService;
import se.vgregion.sitemap.impl.DefaultSitemapGenerator;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class SitemapServiceTest {

    SitemapService sitemapService;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        DefaultSitemapGenerator sitemapGenerator = new DefaultSitemapGenerator();
        MockSitemapCacheLoader cacheLoader = new MockSitemapCacheLoader(null, "http://base.url/target");
        DefaultCacheService sitemapCacheService = new DefaultCacheService(cacheLoader);

        sitemapService = new SitemapService(sitemapGenerator, sitemapCacheService);
    }

    @Test
    public void testGetSitemapContent() {
        String sitemapContent = sitemapService.getSitemapContent();
        assertTrue("Should contain mockId=1",
                sitemapContent.contains("<loc>http://base.url/target?mockId=1</loc>"));
        assertTrue("Should contain mockId=2",
                sitemapContent.contains("<loc>http://base.url/target?mockId=2</loc>"));
    }
}
