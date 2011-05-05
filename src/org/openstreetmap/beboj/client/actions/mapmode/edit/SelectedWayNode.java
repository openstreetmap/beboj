// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.actions.mapmode.edit;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;

public class SelectedWayNode extends ControllerState {
    private Way parentWay;
    private Node node;

    public SelectedWayNode(Way parentWay, Node node) {
        super();
        this.parentWay = parentWay;
        this.node = node;
    }

    @Override
    public ControllerState onMouseDown(MouseDownEvent evt, OsmPrimitive osm) {
//        if (evt.isShiftKeyDown()) { FIXME
//
//        }
        ControllerState cs = sharedOnMouseDown(evt, osm);
        return cs != null ? cs : this;
    }

    @Override
    public ControllerState onMouseMove(MouseMoveEvent evt, OsmPrimitive osm) {
        return this;
    }

    @Override
    public ControllerState onMouseUp(MouseUpEvent evt, OsmPrimitive osm) {
//        if (evt.isShiftKeyDown()) { FIXME
//
//        } else
        if (osm instanceof Node && osm.getReferrers().contains(parentWay)) {
            // select node within way
            boolean isFirst = parentWay.firstNode().equals(node);
            boolean isLast = parentWay.lastNode().equals(node);
            if (isFirst == isLast)
                return new SelectedWayNode(parentWay, node);
            else
                return new DrawWay(parentWay, isLast);


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
        return "SelectedWayNode";
    }
}
