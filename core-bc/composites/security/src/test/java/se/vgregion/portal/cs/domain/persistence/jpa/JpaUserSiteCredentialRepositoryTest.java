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

package se.vgregion.portal.cs.domain.persistence.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.domain.persistence.UserSiteCredentialRepository;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@ContextConfiguration("classpath:JpaRepositoryTest-context.xml")
public class JpaUserSiteCredentialRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserSiteCredentialRepository repo;

    @Before
    public void setup() {
        executeSqlScript("classpath:dbsetup/test-data.sql", false);
    }

    @Test
    public void testGetUserSiteCredential() {
        UserSiteCredential sc1 = repo.getUserSiteCredential("no-test-uid", "test-key");
        assertNull(sc1);

        UserSiteCredential sc2 = repo.getUserSiteCredential("test-uid", "test-key");
        assertNotNull(sc2);
    }

    @Test
    public void testAddUserSiteCredential() {
        UserSiteCredential siteCredential = new UserSiteCredential("new-test-uid", "new-site-key");
        siteCredential.setSiteUser("new-site-user");
        siteCredential.setSitePassword("new-site-password");

        repo.save(siteCredential);

        checkCredential(siteCredential);

        siteCredential.setSitePassword("changed-pw");
        repo.save(siteCredential);

        checkCredential(siteCredential);
    }

    @Test
    public void testAllSiteCredentials() throws Exception {
        assertEquals(2, repo.getAllSiteCredentials("test-uid").size());
        assertEquals(1, repo.getAllSiteCredentials("test-uid2").size());
    }

    private void checkCredential(UserSiteCredential siteCredential) {
        UserSiteCredential response = repo.getUserSiteCredential("new-test-uid", "new-site-key");

        assertEquals(siteCredential, response);
        assertEquals(siteCredential.getUid(), response.getUid());
        assertEquals(siteCredential.getSiteKey(), response.getSiteKey());
        assertEquals(siteCredential.getSiteUser(), response.getSiteUser());
        assertEquals(siteCredential.getSitePassword(), response.getSitePassword());
    }
}
