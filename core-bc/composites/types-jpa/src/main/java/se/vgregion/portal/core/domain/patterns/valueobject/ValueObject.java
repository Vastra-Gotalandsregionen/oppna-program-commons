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
 * <strong>From Wikipedia</strong>: A Value Object is an object that contains attributes but has no conceptual
 * identity. They should be treated as immutable.
 * <p/>
 * <strong>Example</strong>: When people exchange dollar bills, they generally do not distinguish between each
 * unique bill; they only are concerned about the face value of the dollar bill. In this context, dollar bills are
 * value objects. However, the Federal Reserve may be concerned about each unique bill; in this context each bill
 * would be an {@link se.vgregion.portal.core.domain.patterns.entity.Entity entity}.
 * 
 * @param <T>
 *            the Type of the Value Object
 * 
 * @author Anders Asplund - <a href="http://www.callistaenterprise.se">Callista Enterprise</a>
 */
public interface ValueObject<T> extends Serializable {

    /**
     * Value objects compare by the values of their attributes, they don't have an identity.
     * 
     * @param other
     *            The other value object.
     * @return <code>true</code> if the given value object's and this value object's attributes are the same.
     */
    boolean sameValueAs(T other);

}