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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author Anders Asplund - Logica
 * 
 */
@SuppressWarnings({"unchecked", "serial"})
public abstract class AbstractEntity<T extends Entity, ID extends Serializable> implements Entity<T, ID> {

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        if (getId() == null) {
            return super.hashCode();
        }
        return getId().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }

        if (getClass() != other.getClass()) {
            return false;
        }

        T otherType = (T) other;

        if (getId() == null || otherType.getId() == null) {
            return false;
        }

        return new EqualsBuilder().append(otherType.getId(), getId()).isEquals();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
