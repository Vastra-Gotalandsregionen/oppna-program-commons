package se.vgregion.sitemap.servlet;

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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import se.vgregion.sitemap.service.SitemapService;

/**
 * Generates a default sitemap.
 */
public class DefaultSitemapServlet extends HttpServlet {
    private static final long serialVersionUID = -5951290101644382810L;
    private static final String ENCODING_UTF8 = "UTF-8";
    private static final String CLASS_NAME = DefaultSitemapServlet.class.getName();
    private static final Log LOGGER = LogFactory.getLog(DefaultSitemapServlet.class);

    private SitemapService<?> sitemapService;

    /**
     * Get reference to sitemapService from Spring context.
     * 
     * @throws ServletException
     *             if an exception occurs that interrupts the servlet's normal operation.
     */
    @Override
    public void init() throws ServletException {
        LOGGER.info(CLASS_NAME + ".init()");
        super.init();

        WebApplicationContext springContext = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());

        sitemapService = (SitemapService<?>) springContext.getBean("sitemapService");
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.debug(CLASS_NAME + ".doGet()");
        LOGGER.debug("DefaultSitemapServlet starting to put together the sitemap.");

        long startTimeMillis = System.currentTimeMillis();

        String sitemapContent = sitemapService.getSitemapContent();

        long endTimeMillis = System.currentTimeMillis();

        LOGGER.debug("DefaultSitemapServlet generation finished. It took: " + (endTimeMillis - startTimeMillis)
                / 1000 + " seconds.");

        response.setCharacterEncoding(ENCODING_UTF8);

        PrintWriter pw = response.getWriter();
        pw.write(sitemapContent);
        pw.flush();
        pw.close();
    }
}
