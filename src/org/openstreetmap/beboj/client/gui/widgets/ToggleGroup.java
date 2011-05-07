// License: Apache 2.0. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui.widgets;

public class ToggleGroup {
    GroupToggleButton down;

    public void add(GroupToggleButton button) {
        button.setGroup(this);
    }

    public void clicked(GroupToggleButton button) {
        if (down != null) {
            down.setValue(false, false);
        }
        down = button;
    }
}
