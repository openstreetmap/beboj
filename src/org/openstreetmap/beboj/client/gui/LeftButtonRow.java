// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.openstreetmap.beboj.client.gui.widgets.GroupToggleButton;
import org.openstreetmap.beboj.client.gui.widgets.ToggleGroup;

public class LeftButtonRow extends VerticalPanel {

    public LeftButtonRow() {
        getElement().setId("leftButtons");

        Image i1 = new Image("images/dialogs/layerlist.png");
        Image i2 = new Image("images/dialogs/propertiesdialog.png");
        Image i3 = new Image("images/dialogs/selectionlist.png");

        GroupToggleButton t1 = new GroupToggleButton(i1);
        GroupToggleButton t2 = new GroupToggleButton(i2);
        GroupToggleButton t3 = new GroupToggleButton(i3);
        
        ToggleGroup group = new ToggleGroup();
        
        group.add(t1);
        group.add(t2);
        group.add(t3);

        add(t1);
        add(t2);
        add(t3);
    }
}
