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

package se.vgregion.sitemap.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SitemapEntryTest {
    private SitemapEntry sitemapEntry;
    private SitemapEntry otherSitemapEntry;

    @Before
    public void setUp() {
        sitemapEntry = new SitemapEntry("http://localhost", "2010-02-20T16:13:00.000+01:00", "weekly");
        otherSitemapEntry = new SitemapEntry("http://other", "2008-03-11T07:00:00.000+01:00", "weekly");
    }

    @Test
    public void testHashCode() {
        assertEquals(-1301564960, sitemapEntry.hashCode());
        assertEquals(-1127002019, otherSitemapEntry.hashCode());
    }

    @Test
    public void testEqualsObject() {
        assertTrue(sitemapEntry.equals(sitemapEntry));
        assertFalse(sitemapEntry.equals(null));
        assertFalse(sitemapEntry.equals(this));
        assertFalse(sitemapEntry.equals(otherSitemapEntry));
        assertTrue(sitemapEntry.equals(new SitemapEntry("http://localhost", "2008-03-11T07:00:00.000+01:00",
                "weekly")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLoc() {
        sitemapEntry = new SitemapEntry(null, "2010-02-20T16:13:00.000+01:00", "weekly");
    }
}
