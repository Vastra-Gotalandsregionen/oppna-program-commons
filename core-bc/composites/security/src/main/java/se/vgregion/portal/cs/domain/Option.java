package se.vgregion.portal.cs.domain;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-12-27 14:46
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class Option {
    private String value;
    private String display;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
