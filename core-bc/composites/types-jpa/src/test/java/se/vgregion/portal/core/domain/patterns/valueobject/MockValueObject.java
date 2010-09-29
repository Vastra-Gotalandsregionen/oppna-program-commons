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

import java.io.Serializable;

/**
 * This action do that and that, if it has something special it is.
 * 
 * @author Anders Asplund - <a href="http://www.callistaenterprise.se">Callista Enterprise</a>
 */

public class MockValueObject extends AbstractValueObject<MockValueObject> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected MockValueObject() {
        // Used by JPA
    }

    public MockValueObject(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
