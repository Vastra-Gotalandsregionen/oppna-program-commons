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

/**
 * Abstract default implementation common to all concrete implementation of Entity.
 * 
 * @param <T>
 *            The Entity Type
 * @param <ID>
 *            The ID of the Entity
 * 
 * @author Anders Asplund - <a href="http://www.callistaenterprise.se">Callista Enterprise</a>
 * 
 */
public abstract class AbstractEntity<T extends Entity<T, ID>, ID> implements Entity<T, ID> {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean sameAs(final T other) {
        return other != null && this.getId().equals(other.getId());
    }

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
        if (other == this) {
            return true;
        }
        if (getClass() != other.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
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
