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

package se.vgregion.portal.persistance;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.test.util.ReflectionTestUtils;

public class MergingPersistenceUnitPostProcessorTest {
    MergingPersistenceUnitPostProcessor mergingPersistenceUnitPostProcessor = null;
    Map<String, List<String>> puiClasses = new HashMap<String, List<String>>();

    @Before
    public void setUp() throws Exception {
        mergingPersistenceUnitPostProcessor = new MergingPersistenceUnitPostProcessor();

        ReflectionTestUtils.setField(mergingPersistenceUnitPostProcessor, "puiClasses", puiClasses);
    }

    @Test
    public final void testPostProcessPersistenceUnitInfo_emptyCache() {
        MutablePersistenceUnitInfo pui = new MutablePersistenceUnitInfo();
        ReflectionTestUtils.setField(mergingPersistenceUnitPostProcessor, "puiClasses", puiClasses);

        assertEquals(0, puiClasses.size());

        mergingPersistenceUnitPostProcessor.postProcessPersistenceUnitInfo(pui);

        // Assert pui was added
        assertEquals(1, puiClasses.size());
        // We had no classes in either pui or puiClasses cache, assert managed list is empty
        assertEquals(0, pui.getManagedClassNames().size());
    }

    @Test
    public final void testPostProcessPersistenceUnitInfo_populatedCache() {
        MutablePersistenceUnitInfo pui = new MutablePersistenceUnitInfo();
        pui.setPersistenceUnitName("UnitName");

        List<String> list1 = new ArrayList<String>();
        list1.add("ClassName1");
        list1.add("ClassName2");
        puiClasses.put("UnitName", list1);

        List<String> list2 = new ArrayList<String>();
        list2.add("ClassName3");
        list2.add("ClassName4");
        list2.add("ClassName5");
        puiClasses.put("UnitName2", list2);

        assertEquals(2, puiClasses.size());

        mergingPersistenceUnitPostProcessor.postProcessPersistenceUnitInfo(pui);

        // Assert puiClasses cache still contains two elements
        assertEquals(2, puiClasses.size());
        // Assert two class names (ClassName1, ClassName2) were added to pui's ManagedClassName list
        assertEquals(2, pui.getManagedClassNames().size());
        assertEquals("ClassName1", pui.getManagedClassNames().get(0));
    }

}
