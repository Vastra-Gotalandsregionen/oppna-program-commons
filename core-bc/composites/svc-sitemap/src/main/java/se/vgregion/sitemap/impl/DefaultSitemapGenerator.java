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
 */
package se.vgregion.sitemap.impl;

import java.util.List;

import se.vgregion.sitemap.SitemapGenerator;
import se.vgregion.sitemap.model.SitemapEntry;

/**
 * Implementation of SitemapGenerator for external use. The generated XML contains only the base tags from
 * sitemap.org.
 */
public class DefaultSitemapGenerator implements SitemapGenerator {

    public String generate(List<SitemapEntry> sitemapEntries) {
        StringBuilder output = new StringBuilder(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        for (SitemapEntry entry : sitemapEntries) {
            output.append("<url>\n");
            output.append("<loc>").append(entry.getLocation()).append("</loc>\n");
            output.append("<lastmod>").append(entry.getLastModified()).append("</lastmod>\n");
            output.append("<changefreq>").append(entry.getChangeFrequency()).append("</changefreq>\n");
            output.append("<priority>0.5</priority>\n");
            output.append("</url>\n");
        }

        output.append("</urlset>");

        return output.toString();
    }
}
