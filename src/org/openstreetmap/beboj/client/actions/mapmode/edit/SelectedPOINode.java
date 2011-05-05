// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.actions.mapmode.edit;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;

public class SelectedPOINode extends ControllerState {

    private Node initNode;

    public SelectedPOINode(Node initNode) {
        super();
        this.initNode = initNode;
    }

    @Override
    public ControllerState onMouseDown(MouseDownEvent evt, OsmPrimitive osm) {

        if (evt.isControlKeyDown() && !osm.equals(initNode)) {
//            return SelectedMultiple(); FIXME
            return this;
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
        ControllerState cs = sharedOnMouseUp(evt, osm);
        return cs != null ? cs : this;
    }

    @Override
    public void enterState(ControllerState oldState) {
        getCurrentDataSet().setSelected(initNode);
    }

    @Override
    public void exitState(ControllerState newState) {
    }

    @Override
    public String toString() {
        return "SelectedPOINode";
    }

}
