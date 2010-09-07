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
package se.vgregion.sitemap;

import java.util.List;

import se.vgregion.sitemap.model.SitemapEntry;

/**
 * Generates an XML in sitemap.orgs format based on a list of SitemapEntrys.
 */
public interface SitemapGenerator {
    /**
     * Generates an XML in sitemap.orgs format based on the provided list of SitemapEntrys.
     * 
     * @param sitemapEntries
     *            The list of SitemapEntrys to generate the XML for.
     * @return an XML-string in sitemap.orgs format.
     */
    String generate(List<SitemapEntry> sitemapEntries);
}
