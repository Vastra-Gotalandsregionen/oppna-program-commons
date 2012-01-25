package se.vgregion.portal.cs.domain;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-12-27 13:51
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class FormField {
    private String type;
    private String id;
    private String name;
    private String value;
    private List<Option> options;
    private SelectType selectType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public SelectType getSelectType() {
        return selectType;
    }

    public void setSelectType(SelectType selectType) {
        this.selectType = selectType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
