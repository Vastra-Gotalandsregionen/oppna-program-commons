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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import se.vgregion.sitemap.service.SitemapService;

/**
 * Generates a default sitemap.
 */
public class DefaultSitemapServlet extends HttpServlet {
    private static final long serialVersionUID = -5951290101644382810L;
    private static final String ENCODING_UTF8 = "UTF-8";

    private transient Logger logger;
    private transient SitemapService<?> sitemapService;

    /**
     * Get reference to SitemapService from Spring context. Override loadSitemapService to control which service
     * bean to load.
     * 
     * @throws ServletException
     *             if an exception occurs that interrupts the servlet's normal operation.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        loadSitemapService();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        getLogger().debug("doGet(): Starting to put together the sitemap.");

        long startTimeMillis = System.currentTimeMillis();

        String sitemapContent = getSitemapService().getSitemapContent();

        long endTimeMillis = System.currentTimeMillis();

        getLogger().debug(
                "Generation finished. It took: " + (endTimeMillis - startTimeMillis) / 1000 + " seconds.");

        response.setCharacterEncoding(ENCODING_UTF8);

        PrintWriter pw = response.getWriter();
        pw.write(sitemapContent);
        pw.flush();
        pw.close();
    }

    /**
     * Override if you want to load/set other service bean than default "sitemapService" from Spring context.
     */
    protected void loadSitemapService() {
        WebApplicationContext springContext = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());

        setSitemapService((SitemapService<?>) springContext.getBean("sitemapService"));
    }

    /**
     * Setting SitemapService field,primarily to be used when overriding loadSitemapService().
     * 
     * @param sitemapService
     *            The SitemapService to set
     */
    protected void setSitemapService(SitemapService<?> sitemapService) {
        this.sitemapService = sitemapService;
    }

    // Transient...
    protected Logger getLogger() {
        if (logger == null) {
            logger = LoggerFactory.getLogger(this.getClass());
        }
        return logger;
    }

    // Transient...
    private SitemapService<?> getSitemapService() {
        if (sitemapService == null) {
            loadSitemapService();
        }
        return sitemapService;
    }
}
