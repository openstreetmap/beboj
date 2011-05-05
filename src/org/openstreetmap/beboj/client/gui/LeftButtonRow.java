// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openstreetmap.beboj.client.gui.widgets.GroupToggleButton;
import org.openstreetmap.beboj.client.gui.widgets.ToggleGroup;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.mapmode.MapMode;
import org.openstreetmap.josm.gui.MapFrame;

public class LeftButtonRow extends VerticalPanel {

    protected MapFrame mapModesController;

    protected Map<GroupToggleButton, Integer> buttons = new HashMap<GroupToggleButton, Integer>();

    public LeftButtonRow() {
        getElement().setId("leftButtons");

        List<MapMode> modes = Main.platformFactory.getMapModes();

        ToggleGroup group = new ToggleGroup();
        MapModesClickHandler handler = new MapModesClickHandler();
        for (int i=0; i<modes.size(); ++i) {
            MapMode mode = modes.get(i);
            Image img = new Image(mode.getImageUrl());
            GroupToggleButton button = new GroupToggleButton(img);
            add(button);
            buttons.put(button, i);
            group.add(button);
            button.addClickHandler(handler);
        }
    }

    private class MapModesClickHandler implements ClickHandler {

        @Override
        public void onClick(ClickEvent event) {
            int i = buttons.get((GroupToggleButton) event.getSource());
            GWT.log("selected mapmode "+i);
            MapMode selectedMapMode = Main.platformFactory.getMapModes().get(i);
            mapModesController.selectMapMode(selectedMapMode);
        }
    }

}
