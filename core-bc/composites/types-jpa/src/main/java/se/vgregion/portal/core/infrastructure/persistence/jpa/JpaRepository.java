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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.portal.core.domain.patterns.entity.Entity;
import se.vgregion.portal.core.infrastructure.persistence.DatabaseRepository;

/**
 * An abstract default implementation of a {@link DatabaseRepository}. This is an JPA implementation used with any
 * JPA implementation such as Hibernate.
 * 
 * @param <T>
 *            The Entity Type
 * @param <ID>
 *            The ID of the Entity
 * @param <PK>
 *            The type of the primary key
 * 
 * @author Anders Asplund - <a href="http://www.callistaenterprise.se">Callista Enterprise</a>
 * 
 */
public abstract class JpaRepository<T extends Entity<T, ID>, ID extends Serializable, PK extends Serializable>
        implements DatabaseRepository<T, ID, PK> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaRepository.class);

    /**
     * Entity manager ref.
     */
    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * Entity class type.
     */
    protected Class<T> type;

    public void setType(Class<T> type) {
        this.type = type;
    }

    /**
     * Default constructor.
     */
    @SuppressWarnings("unchecked")
    public JpaRepository() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Parameterized constructor.
     * 
     * @param type
     *            Entity class type
     */
    public JpaRepository(Class<T> type) {
        this.type = type;
    }

    /**
     * Does entity manager contain entity?
     * 
     * @param entity
     *            Entity to check for
     * @return true if found
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public boolean contains(T entity) {
        return entityManager.contains(entity);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<T> findAll() {
        Query query = entityManager.createQuery("select o from " + type.getSimpleName() + " o");
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<T> findByNamedQuery(String queryName, Map<String, ? extends Object> args) {
        Query namedQuery = entityManager.createNamedQuery(queryName);
        for (Map.Entry<String, ? extends Object> parameter : args.entrySet()) {
            namedQuery.setParameter(parameter.getKey(), parameter.getValue());
        }
        return namedQuery.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<T> findByNamedQuery(String queryName, Object[] args) {
        Query namedQuery = entityManager.createNamedQuery(queryName);
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                namedQuery.setParameter(i + 1, args[i]);
            }
        }
        return namedQuery.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public T findInstanceByNamedQuery(String queryName, Object[] args) {
        Query namedQuery = entityManager.createNamedQuery(queryName);
        if (args != null) {
            int position = 1;
            for (Object value : args) {
                // will throw IllegalArgumentException if position is incorrect
                // or value is incorrect type
                namedQuery.setParameter(position++, value);
            }
        }

        try {
            @SuppressWarnings("unchecked")
            T result = (T) namedQuery.getSingleResult();
            return result;
        } catch (NoResultException nre) {
            LOGGER.warn("No entiry found for query: {}", namedQuery);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public T findInstanceByNamedQuery(String queryName, Map<String, ? extends Object> args) {
        Query namedQuery = entityManager.createNamedQuery(queryName);
        if (args != null) {
            for (Map.Entry<String, ? extends Object> entry : args.entrySet()) {
                // will throw IllegalArgumentException if parameter name not
                // found or value is incorrect type
                namedQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }
        try {
            @SuppressWarnings("unchecked")
            T result = (T) namedQuery.getSingleResult();
            return result;
        } catch (NoResultException nre) {
            LOGGER.warn("No entity found for query: {}", namedQuery);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated Deprecated as of types-jpa 3.0, replaced by {@link JpaRepository#find(Serializable)}. This
     *             method will be removed in version 3.2.
     */
    @Deprecated
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public T findByPk(ID pk) {
        return entityManager.find(type, pk);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public T find(ID id) {
        return entityManager.find(type, id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public T findByPrimaryKey(PK pk) {
        return entityManager.find(type, pk);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void flush() {
        entityManager.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public T persist(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void clear() {
        entityManager.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated Deprecated as of types-jpa 3.0, replaced by {@link JpaRepository#remove(Serializable)}. This
     *             method will be removed in version 3.2.
     */
    @Deprecated
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByPk(ID taskId) {
        T entity = entityManager.find(type, taskId);
        entityManager.remove(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void remove(ID id) {
        T entity = find(id);
        entityManager.remove(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeByPrimaryKey(PK pk) {
        T entity = findByPrimaryKey(pk);
        entityManager.remove(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public T merge(T entity) {
        return entityManager.merge(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public T store(T entity) {
        if (entity.getId() == null || entityManager.find(type, entity.getId()) == null) {
            persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void refresh(T entity) {
        entityManager.refresh(entity);
    }
}
