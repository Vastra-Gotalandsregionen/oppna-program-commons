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

package se.vgregion.portal.core.domain.patterns.entity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * This action do that and that, if it has something special it is.
 * 
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class AbstractEntityTest {
    AbstractEntity<TestEntity, Long> entity1;
    AbstractEntity<TestEntity, Long> entity2;
    AbstractEntity<TestEntity, Long> entitySame1;
    AbstractEntity<TestEntity, Long> entityNull1;
    AbstractEntity<TestEntity, Long> entityNull2;

    @Before
    public void setUp() {
        entity1 = new TestEntity(1L);
        entity2 = new TestEntity(2L);
        entitySame1 = new TestEntity(1L);
        entityNull1 = new TestEntity(null);
        entityNull2 = new TestEntity(null);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(1, entity1.hashCode());
        assertEquals(entity1.hashCode(), entitySame1.hashCode());
        assertNotSame(entity1.hashCode(), entity2.hashCode());
        assertNotSame(entityNull1.hashCode(), entityNull2.hashCode());
    }

    @Test
    public void testEquals() throws Exception {
        assertTrue(entity1.equals(entitySame1));
        assertFalse(entity1.equals(entity2));
        assertFalse(entityNull1.equals(entityNull2));
        assertTrue(entityNull1.equals(entityNull1));
        assertFalse(entity1.equals(entityNull1));
        assertFalse(entity1.equals(new Object()));
        assertFalse(entity1.equals(null));
    }

    @Test
    public void testToString() throws Exception {
        assertTrue(entity1.toString().contains("id=1"));
    }

    private class TestEntity extends AbstractEntity<TestEntity, Long> {

        private static final long serialVersionUID = 1L;
        private Long id;

        @Override
        public Long getId() {
            return id;
        }

        public TestEntity(Long id) {
            this.id = id;
        }
    }
}
