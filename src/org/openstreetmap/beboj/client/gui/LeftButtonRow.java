// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.List;

import org.openstreetmap.beboj.client.gui.widgets.GroupToggleButton;
import org.openstreetmap.beboj.client.gui.widgets.ToggleGroup;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.mapmode.MapMode;
import org.openstreetmap.josm.gui.MapFrame;

public class LeftButtonRow extends VerticalPanel {

    protected MapFrame mapModesController;

    public GroupToggleButton[] buttons;

    public LeftButtonRow() {
        getElement().setId("leftButtons");
        setSpacing(2);

        List<MapMode> modes = Main.platformFactory.getMapModes();

        ToggleGroup group = new ToggleGroup();
        buttons = new GroupToggleButton[modes.size()];
        MapModesClickHandler handler = new MapModesClickHandler();
        for (int i=0; i<modes.size(); ++i) {
            MapMode mode = modes.get(i);
            Image img = new Image(mode.getImageUrl());
            GroupToggleButton button = new GroupToggleButton(img);
            add(button);
            buttons[i] = button;
            group.add(button);
            button.addClickHandler(handler);
        }
    }

    private class MapModesClickHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            int idx = -1;
            for (int i=0; i<buttons.length; ++i) {
                if (event.getSource() == buttons[i]) {
                    idx = i;
                    break;
                }
            }
            if (idx == -1)
                throw new AssertionError();
            GWT.log("selected mapmode "+idx);
            MapMode selectedMapMode = Main.platformFactory.getMapModes().get(idx);
            mapModesController.selectMapMode(selectedMapMode);
        }
    }
}
