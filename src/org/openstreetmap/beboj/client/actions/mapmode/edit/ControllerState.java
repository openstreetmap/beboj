// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.actions.mapmode.edit;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

import java.util.Collection;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;

public abstract class ControllerState {
//    protected EditController controller;
    protected ControllerState previousState;

    abstract public ControllerState onMouseDown(MouseDownEvent evt, OsmPrimitive osm);
    abstract public ControllerState onMouseMove(MouseMoveEvent evt, OsmPrimitive osm);
    abstract public ControllerState onMouseUp(MouseUpEvent evt, OsmPrimitive osm);

    abstract public void enterState(ControllerState oldState);
    abstract public void exitState(ControllerState newState);

    protected DataSet getCurrentDataSet() {
        return Main.main.getCurrentDataSet();
    }

    public ControllerState sharedOnMouseDown(MouseDownEvent evt, OsmPrimitive osm) {
        if (osm == null) {
            return new DragMap(evt.getX(), evt.getY());
        } else {
            Way selectedWay = null;
            Collection<OsmPrimitive> sel = getCurrentDataSet().getSelected();
            if (!sel.isEmpty()) {
                OsmPrimitive fst = sel.iterator().next();
                if (fst instanceof Way) {
                    selectedWay = (Way) fst;
                }
            }

            if (osm instanceof Node && selectedWay != null && osm.getReferrers().contains(selectedWay)) {
                getCurrentDataSet().setSelected(osm);
                return new DragWayNode(selectedWay, (Node) osm, evt.getX(), evt.getY());
            }
            // FIXME: what is this?
//        } else if ( entity is Node && focus is Way ) {
//            // select way node
//            return new DragWayNode(focus as Way, getNodeIndex(focus as Way,entity as Node), event, false);

            else if (osm != null) {
                if (!osm.isSelected()) {
                    Main.main.getCurrentDataSet().setSelected(osm);
                }
                return new DragSelection(evt.getX(), evt.getY());
            }
        }

//        OsmPrimitive focus = getTopLevelFocusEntity(entity);
//        Way selectedWay = getSelectedWay();
//        // FIXME: optimize referrer
//        if (entity instanceof Node && selectedWay != null && entity.getReferrers().contains(selectedWay)) {
//            // select node within this way
//            return new DragWayNode(selectedWay, getNodeIndex(selectedWay, (Node) entity), e, false);
//        } else if (entity instanceof Node && focus instanceof Way) {
//           // select way node
//            return new DragWayNode((Way) focus, getNodeIndex((Way) focus, (Node) entity), e, false);
//        } else if (entity != null) {
//            if (!entity.isSelected()) {
//                Main.main.getCurrentDataSet().setSelected(entity);
//            }
//            return new DragSelection(e.getX(), e.getY());
//        }
        return null;
    }

    protected ControllerState sharedOnMouseUp(MouseUpEvent e, OsmPrimitive entity) {
//        OsmPrimitive focus = getTopLevelFocusEntity(entity);
        if (entity == null) {
            getCurrentDataSet().clearSelection();
            return this instanceof NoSelection ? this : new NoSelection();
        }
//        // FIXME
////        if (focus != null && controller.getDragstate() != DragState.NOT_DRAGGING) {
////            controller.onMouseUp(null); // in case the end-drag is over an EntityUI
////        }
        return null;
    }

    // this is used to ignore nodes on selection and only react to ways
    // FIXME: this is kinda hackish, we shouldn't hit nodes in the first place
    protected static OsmPrimitive getTopLevelFocusEntity(OsmPrimitive osm) {
        if (osm instanceof Node) {
            for (OsmPrimitive parent : osm.getReferrers()) {
                if (parent instanceof Way)
                    return parent;
            }
            return osm;
        } else if (osm instanceof Way) {
            return osm;
        } else
            return null;
    }

    protected static ControllerState findStateForSelection() {
        //FIXME
        Collection<OsmPrimitive> sel = Main.main.getCurrentDataSet().getSelected();
        if (sel.isEmpty())
            return new NoSelection();
//        else if (sel.size() > 1)
//            return new SelectedMultiple();
        OsmPrimitive s = sel.iterator().next();
        if (s instanceof Way)
            return new SelectedWay((Way) s);
        else if (s instanceof Node) {
            Node n = (Node) s;
//            Way parentWay = firstParentWay(n);
//            if (parentWay != null)
//                return new SelectedWayNode(parentWay, ControllerState.getNodeIndex(parentWay, n));
//            else
                return new SelectedPOINode(n);
        }
        // FIXME
        throw new RuntimeException();
    }


}
