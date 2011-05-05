// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.actions.mapmode.edit;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;

public class SelectedWay extends ControllerState {

    Way initWay;

    public SelectedWay(Way initWay) {
        super();
        this.initWay = initWay;
    }

    @Override
    public ControllerState onMouseDown(MouseDownEvent evt, OsmPrimitive osm) {
        OsmPrimitive focus = getTopLevelFocusEntity(osm);
        if (osm instanceof Way && evt.isShiftKeyDown() && initWay.equals(focus)) {
            // insert node within way (shift-click)
            // FIXME
        } else if (evt.isControlKeyDown() && osm != null && !initWay.equals(osm)) {
            // multiple selection
            // FIXME
        }
        ControllerState cs = sharedOnMouseDown(evt, osm);
        return cs != null ? cs : this;
    }

    @Override
    public ControllerState onMouseMove(MouseMoveEvent evt, OsmPrimitive osm) {
        return this;
    }

    @Override
    public ControllerState onMouseUp(MouseUpEvent evt, OsmPrimitive osm) {
        if (evt.isShiftKeyDown()) {
            //FIXME draw new way
        }
        ControllerState cs = sharedOnMouseUp(evt, osm);
        return cs != null ? cs : this;
    }

    @Override
    public void enterState(ControllerState oldState) {
        // TODO Auto-generated method stub
    }

    @Override
    public void exitState(ControllerState newState) {
        // TODO Auto-generated method stub
    }

    @Override
    public String toString() {
        return "SelectedWay";
    }

}
