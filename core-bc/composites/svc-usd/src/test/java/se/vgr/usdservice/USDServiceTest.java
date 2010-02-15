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

package se.vgr.usdservice;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.axis.AxisFault;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ca.www.UnicenterServicePlus.ServiceDesk.USD_WebServiceSoapSoapBindingStub;

public class USDServiceTest extends TestCase {

    boolean integrationTest = false;
    private USDService usdService;

    @Override
    @Before
    public void setUp() throws Exception {

        Properties p = new Properties();
        p.setProperty("endpoint", "http://apache.org");
        Properties mappings = new Properties();

        if (integrationTest) {
            p.load(ClassLoader.getSystemResourceAsStream("usd.properties"));
            mappings.load(ClassLoader.getSystemResourceAsStream("usdAppToGroup.properties"));
            usdService = new USDServiceImpl(p);
        }
        else {
            usdService = new USDServiceImpl(p) {

                @Override
                protected USD_WebServiceSoapSoapBindingStub getWebService() {
                    try {
                        return new Mock_USD_WebServiceSoapSoapBindingStub();
                    }
                    catch (AxisFault e) {
                        throw new RuntimeException("TODO: Handle this exception better", e);
                    }
                }

                @Override
                protected String getHandleFromResponse(InputStream xml) throws Exception {
                    return "";
                }

            };

        }
        ((USDServiceImpl) usdService).setUsdAppToGroupMappings(mappings);
    }

    public Properties getTestParameters() {
        Properties p = new Properties();
        p.setProperty("affected_resource", "nr:BF5880E3AF1C8542B2546B93922C25A7");
        p.setProperty("category", "pcat:400023");
        p.setProperty("description", "Testing test test description");
        String appName = "Tyck till test portlet".trim().replaceAll(" ", "_");
        System.out.println("getting group for appName=" + appName);
        String groupHandle = "abc";
        if (integrationTest) {
            groupHandle = usdService.getUSDGroupHandleForApplicationName(appName);
        }
        p.setProperty("group", groupHandle);
        p.setProperty("impact", "imp:1603");
        p.setProperty("priority", "pri:500");
        p.setProperty("type", "crt:182");
        p.setProperty("urgency", "urg:1100");
        p.setProperty("z_location", "loc:67F817D782E87B45A8298FC5512B6A9C");
        p.setProperty("z_organization", "org:5527E3F8D19F49409036F162493C7DD0");
        p.setProperty("z_telefon_nr", "031-123456");
        return p;
    }

    @Override
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreateRequest() {

        String result = usdService.createRequest(getTestParameters(), "andcu1", null);
        assertNotNull(result);

    }

    @Test
    public void testCreateRequestWithAttachments() {
        if (integrationTest) {

            List<File> files = new ArrayList<File>();
            File file;
            try {
                file = File.createTempFile("attachtestäöå", ".doc", new File("c:/program files/temp"));
            }
            catch (IOException e) {
                throw new RuntimeException("TODO: Handle this exception better", e);
            }

            files.add(file);

            String result = usdService.createRequest(getTestParameters(), "andcu1", files);
            System.out.println("result=" + result);
            assertNotNull(result);
            file.delete();

        }

    }

}
