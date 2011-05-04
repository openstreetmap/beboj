// License: GPL. For details, see LICENSE file
package javax.swing;

import java.awt.event.ActionEvent;
import java.util.HashMap;

public abstract class AbstractAction implements Action {

    public static final String SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
    public static final String SMALL_ICON = "SMALL_ICON";
    public static final String NAME = "NAME";

    private final HashMap<String, Object> props = new HashMap<String, Object>();

    public AbstractAction() {
    }

    @Override
    abstract public void actionPerformed(ActionEvent e);

    @Override
    public void putValue(String key, Object val) {
        props.put(key, val);
    }
}
