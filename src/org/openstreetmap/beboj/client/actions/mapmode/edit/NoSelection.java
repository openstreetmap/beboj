// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.actions.mapmode.edit;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

import org.openstreetmap.josm.data.osm.OsmPrimitive;

public class NoSelection extends ControllerState {

    @Override
    public ControllerState onMouseDown(MouseDownEvent evt, OsmPrimitive osm) {
        ControllerState cs = sharedOnMouseDown(evt, osm);
        return cs != null ? cs : this;

//        if (osm == null) {
//            return new DragMap(evt.getX(), evt.getY());
//        } else {
//            return this; //FIXME
//        }
    }

    @Override
    public ControllerState onMouseMove(MouseMoveEvent evt, OsmPrimitive osm) {
        return this; // FIXME hover stuff
    }

    @Override
    public ControllerState onMouseUp(MouseUpEvent evt, OsmPrimitive osm) {
        // FIXME: happens, but cannot reproduce so far
        throw new UnsupportedOperationException();
    }

    @Override
    public void enterState(ControllerState oldState) {
    }

    @Override
    public void exitState(ControllerState newState) {
    }

    @Override
    public String toString() {
        return "NoSelection";
    }

}
