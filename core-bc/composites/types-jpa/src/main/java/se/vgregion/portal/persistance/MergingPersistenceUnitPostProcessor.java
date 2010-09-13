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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

/**
 * @author Anders Asplund - Callista Enterprise
 * @author David Rosell - Redpill-Linpro
 * 
 */
public class MergingPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {
    private Map<String, List<String>> puiClasses = new HashMap<String, List<String>>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
        List<String> classes = puiClasses.get(pui.getPersistenceUnitName());
        if (classes == null) {
            classes = new ArrayList<String>();
            puiClasses.put(pui.getPersistenceUnitName(), classes);
        }

        pui.getManagedClassNames().addAll(classes);
        classes.addAll(pui.getManagedClassNames());
    }
}
