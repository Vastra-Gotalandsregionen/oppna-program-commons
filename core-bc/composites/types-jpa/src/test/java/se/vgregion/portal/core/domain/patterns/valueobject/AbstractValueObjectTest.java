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

package se.vgregion.portal.core.domain.patterns.valueobject;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * This action do that and that, if it has something special it is.
 * 
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class AbstractValueObjectTest {
    private String voName1 = "voName1";
    private String voName2 = "voName2";

    private MockValueObject vo1;
    private MockValueObject vo2;
    private MockValueObject voSame1;
    private MockValueObject voNull1;
    private MockValueObject voNull2;

    @Before
    public void setUp() {
        vo1 = new MockValueObject(voName1);
        vo2 = new MockValueObject(voName2);
        voSame1 = new MockValueObject(voName1);
        voNull1 = new MockValueObject(null);
        voNull2 = new MockValueObject(null);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(vo1.hashCode(), voSame1.hashCode());
        assertNotSame(vo1.hashCode(), vo2.hashCode());
        assertNotSame(voNull1.hashCode(), voNull2.hashCode());
    }

    @Test
    public void testEquals() throws Exception {
        assertTrue(vo1.equals(voSame1));
        assertFalse(vo1.equals(vo2));
        assertTrue(voNull1.equals(voNull2));
        assertTrue(voNull1.equals(voNull1));
        assertFalse(vo1.equals(voNull1));
        assertFalse(vo1.equals(new Object()));
        assertFalse(vo1.equals(null));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(vo1.toString(), equalTo("MockValueObject[name=" + voName1 + "]"));
    }

    @Test
    public void testSameAs() throws Exception {
        assertTrue(vo1.sameValueAs(voSame1));
        assertFalse(vo1.sameValueAs(vo2));
    }

}
