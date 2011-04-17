// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LeftButtonRow extends VerticalPanel {

    public LeftButtonRow() {
        getElement().setId("leftButtons");

        Image i1 = new Image("images/dialogs/layerlist.png");
        Image i2 = new Image("images/dialogs/propertiesdialog.png");
        Image i3 = new Image("images/dialogs/selectionlist.png");

        ToggleButton t1 = new ToggleButton(i1);
        ToggleButton t2 = new ToggleButton(i2);
        ToggleButton t3 = new ToggleButton(i3);

        add(t1);
        add(t2);
        add(t3);
    }
}
