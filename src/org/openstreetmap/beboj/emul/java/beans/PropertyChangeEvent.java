// License: GPL. For details, see LICENSE file
package java.beans;

public class PropertyChangeEvent {
    private final Object src;
    private final String prop;
    private final Object oldVal;
    private final Object newVal;

    public PropertyChangeEvent(Object src, String prop, Object oldVal, Object newVal) {
        this.src = src;
        this.prop = prop;
        this.oldVal = oldVal;
        this.newVal = newVal;
    }

    public String getPropertyName() {
        return prop;
    }

    public Object getSource() {
        return src;
    }
}
