// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.actions.mapmode.edit;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

import org.openstreetmap.beboj.client.Beboj;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.command.AddCommand;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;

public class DragMap extends ControllerState {

    public enum DragState {
        DRAGGING,       /** While moving. */
        NOT_DRAGGING,   /** not in "Dragging" process */
        NOT_MOVED       /** "Dragging" but hasn't actually moved yet. */
    }

    private DragState dragstate;
    private int lastxmouse;
    private int lastymouse;
    private int downX;
    private int downY;
    private long downTime;
    private final static int TOLERANCE = 7;

    public DragMap(int downX, int downY) {
        super();
        this.downX = downX;
        this.downY = downY;
    }

    @Override
    public ControllerState onMouseDown(MouseDownEvent evt, OsmPrimitive osm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ControllerState onMouseMove(MouseMoveEvent evt, OsmPrimitive osm) {
        if (dragstate == DragState.NOT_MOVED) {
            if (System.currentTimeMillis() - downTime < 300l) {
                if (Math.abs(downX - evt.getX()) <= TOLERANCE && Math.abs(downY - evt.getY()) <= TOLERANCE)
                    return this;
            } else {
                if (Math.abs(downX - evt.getX()) <= TOLERANCE / 2 && Math.abs(downY - evt.getY()) <= TOLERANCE / 2)
                    return this;
            }
            setDragstate(DragState.DRAGGING);
        }

        if (lastxmouse != evt.getX() || lastymouse != evt.getY()) {
            // FIXME: to be 100% exact, the calculation should be in device coordinates
            EastNorth mousePos = Main.map.mapView.getEastNorth(evt.getX(), evt.getY());
            EastNorth center = Main.map.mapView.nav.getCenter();
            EastNorth lastMousePos = Main.map.mapView.getEastNorth(lastxmouse, lastymouse);
            Main.map.mapView.nav.zoomTo(new EastNorth(
                    center.east() + lastMousePos.east() - mousePos.east(),
                    center.north() + lastMousePos.north() - mousePos.north()));

            Beboj.canvasView.repaint(); // use listener

            lastxmouse = evt.getX();
            lastymouse = evt.getY();
        }
        return this;
    }

    @Override
    public ControllerState onMouseUp(MouseUpEvent evt, OsmPrimitive osm) {
        switch (dragstate) {
            case NOT_MOVED:
                if (previousState instanceof NoSelection) {
                    Node startNode = new Node(Main.map.mapView.getLatLon(evt.getX(), evt.getY()));
                    Main.main.undoRedo.add(new AddCommand(startNode));
                    Beboj.canvasView.repaint();
                    return new DrawWay(startNode);
                } else {
                    getCurrentDataSet().clearSelection();
                    return new NoSelection();
                }
            case DRAGGING:
                return previousState;//new NoSelection();
            default:
                throw new UnsupportedOperationException();
        }
    }

    protected void setDragstate(DragState dragstate) {
        this.dragstate = dragstate;
        Beboj.debug("DragMap.dragstate", dragstate.toString());
    }

    @Override
    public void enterState(ControllerState oldState) {
        setDragstate(DragState.NOT_MOVED);
        lastxmouse = downX;
        lastymouse = downY;
        downTime = System.currentTimeMillis();
        previousState  = oldState;
    }

    @Override
    public void exitState(ControllerState newState) {
        Beboj.remove_debug("DragMap.dragstate");
    }

    @Override
    public String toString() {
        return "DragMap";
    }

}
