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
package se.vgregion.sitemap.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class SitemapCacheTest {
    private SitemapCache sitemapCache;
    private SitemapEntry sitemapEntry;
    private SitemapEntry otherSitemapEntry;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        sitemapCache = new SitemapCache();
        sitemapEntry = new SitemapEntry("http://localhost", "2010-02-20T16:13:00.000+01:00", "weekly");
        otherSitemapEntry = new SitemapEntry("http://other", "2008-03-11T07:00:00.000+01:00", "weekly");
    }

    @Test
    public final void initializedEmpty() {
        assertNotNull(sitemapCache.getEntries());
        assertEquals(0, sitemapCache.getEntries().size());
    }

    @Test
    public final void immutableMapReturned() {
        try {
            List<SitemapEntry> result = sitemapCache.getEntries();
            result.add(sitemapEntry);
            fail("Modification should result in exception");
        } catch (UnsupportedOperationException e) {
            // exception ok
        }
    }

    @Test
    public final void addIllegalEntries() {
        try {
            sitemapCache.add(null);
            fail("Illegal entries are not allowed");
        } catch (IllegalArgumentException e) {
            // exception ok
        }
    }

    /**
     * Test method for {@link se.vgregion.sitemap.model.SitemapCache#getEntries()}.
     */
    @Test
    public final void addGetEntries() {
        sitemapCache.add(sitemapEntry);
        assertEquals(1, sitemapCache.getEntries().size());

        sitemapCache.add(otherSitemapEntry);
        assertEquals(2, sitemapCache.getEntries().size());

        sitemapCache.add(sitemapEntry);
        assertEquals(2, sitemapCache.getEntries().size());
    }
}
