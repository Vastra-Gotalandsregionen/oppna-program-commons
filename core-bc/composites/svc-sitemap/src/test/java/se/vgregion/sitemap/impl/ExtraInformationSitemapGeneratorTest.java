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

import se.vgregion.sitemap.model.SitemapEntry;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class ExtraInformationSitemapGeneratorTest {

    private ExtraInformationSitemapGenerator sitemapGenerator;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        sitemapGenerator = new ExtraInformationSitemapGenerator();
    }

    /**
     * Test method for {@link se.vgregion.sitemap.impl.ExtraInformationSitemapGenerator#generate(java.util.List)}.
     */
    @Test
    public final void testGenerate() {
        String sitemapContent = sitemapGenerator.generate(poulateEntries());
        assertTrue("Should contain at least one changefreq",
                sitemapContent.contains("<changefreq>daily</changefreq>"));

        assertTrue("Should contain loc for first entry",
                sitemapContent.contains("<loc>http://www.domain.com/target?id=1</loc>"));
        assertTrue("Should contain name for first entry",
                sitemapContent.contains("<extrainfo:name>Kalle</extrainfo:name>"));

        assertTrue("Should contain lastmod for second entry",
                sitemapContent.contains("<lastmod>2010-02-01T02:00:00+01:00</lastmod>"));
        assertTrue("Should contain blame for second entry",
                sitemapContent.contains("<extrainfo:blame>Yes</extrainfo:blame>"));
    }

    private List<SitemapEntry> poulateEntries() {
        SitemapEntry entry;
        List<SitemapEntry> sitemapEntries = new ArrayList<SitemapEntry>();

        entry = new SitemapEntry("http://www.domain.com/target?id=1", "2010-02-01T01:00:00+01:00", "daily");
        entry.addExtraInformation("name", "Kalle");
        entry.addExtraInformation("blame", "No");
        sitemapEntries.add(entry);
        entry = new SitemapEntry("http://www.domain.com/target?id=2", "2010-02-01T02:00:00+01:00", "daily");
        entry.addExtraInformation("name", "Pelle");
        entry.addExtraInformation("blame", "Yes");
        sitemapEntries.add(entry);

        return sitemapEntries;
    }
}
