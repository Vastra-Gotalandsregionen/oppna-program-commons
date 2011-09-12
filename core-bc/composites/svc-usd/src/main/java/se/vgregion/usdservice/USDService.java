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

package se.vgregion.usdservice;

import se.vgregion.usdservice.domain.Issue;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import se.vgregion.util.Attachment;

/**
 * 
 * @author David Rosell - Redpill-Linpro
 * @author Andrew Culbert
 *
 */
public interface USDService {

    String createRequest(Properties testParameters, String string, Collection<Attachment> attachments);

    String getUSDGroupHandleForApplicationName(String appName);

    List<Issue> lookupRecords(String userId, Integer maxRows);

}
