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

package se.vgregion.portal.core.infrastructure.persistence.jpa;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import se.vgregion.portal.core.domain.patterns.entity.MockEntity;
import se.vgregion.portal.core.domain.patterns.entity.MockEntityRepository;

/**
 * This action do that and that, if it has something special it is.
 * 
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 * @author Anders Asplund - Callista Enterprise
 */
@ContextConfiguration("classpath:JpaMockEntityRepositoryTest-context.xml")
public class JpaMockEntityRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private MockEntityRepository testRepository;

    @Before
    public void setUp() throws Exception {
        executeSqlScript("classpath:dbsetup/test-data.sql", false);
    }

    @After
    public void tearDown() throws Exception {
        executeSqlScript("classpath:dbsetup/drop-test-data.sql", false);
    }

    @Test
    @Rollback(false)
    public void findByPrimaryKey() {
        MockEntity entity = testRepository.find(1L);

        assertEquals("entityName1", entity.getName());
    }

    @Test
    @Rollback(false)
    public void findAll() {
        List<MockEntity> entityList = testRepository.findAll();

        assertEquals(2, entityList.size());
    }

    @Test
    @Rollback(false)
    public void merge() {
        MockEntity entity = testRepository.find(1L);
        entity.setName("newName");
        testRepository.merge(entity);
        testRepository.flush();

        entity = testRepository.find(1L);

        assertEquals("newName", entity.getName());
    }

    @Test
    @Rollback(false)
    public void removeEntity() {
        List<MockEntity> entityList = testRepository.findAll();

        testRepository.remove(entityList.get(0));

        testRepository.flush();

        entityList = testRepository.findAll();

        assertEquals(1, entityList.size());
    }

    @Test
    @Rollback(false)
    public void deleteByPk() {
        testRepository.remove(2L);

        testRepository.flush();

        List<MockEntity> entityList = testRepository.findAll();

        assertEquals(1, entityList.size());
        assertEquals(new Long(1), entityList.get(0).getId());
    }

    @Test
    @Rollback(false)
    public void refresh() {
        MockEntity entity = testRepository.find(1L);

        entity.setName("newName");

        testRepository.refresh(entity);

        assertEquals("entityName1", entity.getName());
    }

    @Test
    @Rollback(false)
    public void persist() {
        MockEntity entity = new MockEntity();
        entity.setName("newentity");

        testRepository.persist(entity);
        testRepository.flush();

        List<MockEntity> entityList = testRepository.findAll();

        assertEquals(3, entityList.size());
        for (MockEntity j : entityList) {
            assertNotNull(j.getId());
        }
    }

    @Test
    @Rollback(false)
    public void clear() {
        MockEntity entity = testRepository.findByPk(1L);

        entity.setName("newName");
        assertTrue(testRepository.contains(entity));

        testRepository.clear();

        assertFalse(testRepository.contains(entity));

        MockEntity entityAgain = testRepository.findByPk(1L);
        assertEquals("entityName1", entityAgain.getName());
    }

    @Test
    @Rollback(false)
    public void store() {
        MockEntity entity = testRepository.findByPk(1L);

        entity.setName("newName");

        testRepository.store(entity);

        List<MockEntity> entityList = testRepository.findAll();
        assertEquals(2, entityList.size());

        MockEntity newentity = new MockEntity();
        newentity.setName("newentity");

        testRepository.store(newentity);

        entityList = testRepository.findAll();
        assertEquals(3, entityList.size());

    }
}
