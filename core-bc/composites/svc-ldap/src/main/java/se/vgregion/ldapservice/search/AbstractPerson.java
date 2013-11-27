package se.vgregion.ldapservice.search;

import org.apache.commons.beanutils.BeanMap;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: clalu4
 * Date: 2013-11-27
 * Time: 07:50
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPerson implements Serializable {

    private final BeanMap bm = new BeanMap(this);

    @Override
    public String toString() {
        return new TreeMap<String, Object>(bm).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof AbstractPerson)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        BeanMap otherMap = new BeanMap(obj);
        return bm.equals(otherMap);
    }

    @Override
    public int hashCode() {
        return bm.hashCode();
    }
}
