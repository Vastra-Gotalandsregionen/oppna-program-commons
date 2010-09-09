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
package se.vgregion.sitemap.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Format Date to W3C format
 */
public class W3CDateTimeUtil {
    public static final String W3C_TIME = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static final String formatDateW3C(Date date) {
        String formattedDate = "";
        if (date != null) {
            String timeStamp = new SimpleDateFormat(W3C_TIME).format(date);
            formattedDate = timeStamp.substring(0, 22) + ":" + timeStamp.substring(22);
        }
        return formattedDate;
    }

    public static final String getLastModifiedW3CDateTime(Date modifyTimestamp, Date createTimestamp) {
        Date lastModified = modifyTimestamp;

        if (modifyTimestamp == null) {
            lastModified = createTimestamp;
        }

        return formatDateW3C(lastModified);
    }
}
