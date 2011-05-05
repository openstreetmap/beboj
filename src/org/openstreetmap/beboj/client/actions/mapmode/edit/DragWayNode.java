// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.actions.mapmode.edit;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

import java.util.Collections;

import org.openstreetmap.beboj.client.Beboj;
import org.openstreetmap.beboj.client.actions.mapmode.edit.DragMap.DragState;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.command.MoveCommand;
import org.openstreetmap.josm.command.SequenceCommand;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;

public class DragWayNode extends ControllerState {

    private long enterTime; //FIXME use this
    private int downX, downY;
    private DragState dragstate;

    private boolean needFreshMoveCommand;

    private Way parentWay;
    private Node dragNode;

    public DragWayNode(Way parentWay, Node dragNode, int downX, int downY) {
        super();
        this.parentWay = parentWay;
        this.dragNode = dragNode;
        this.downX = downX;
        this.downY = downY;
    }

    @Override
    public ControllerState onMouseDown(MouseDownEvent evt, OsmPrimitive osm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ControllerState onMouseMove(MouseMoveEvent evt, OsmPrimitive osm) {
        if (dragstate==DragState.NOT_MOVED)
            if ((Math.abs(downX - evt.getX()) < 3 && Math.abs(downY - evt.getY()) < 3) ||
                    System.currentTimeMillis() - enterTime < 300l) {
            return this;
        }
        dragstate=DragState.DRAGGING;
        return dragTo(evt.getX(), evt.getY());
    }

    @Override
    public ControllerState onMouseUp(MouseUpEvent evt, OsmPrimitive osm) {
        if (dragstate == DragState.DRAGGING) {
            // mouse-up while dragging, so end drag
            return new SelectedWayNode(parentWay, dragNode);
//        } else if (evt.isShiftKeyDown()) { FIXME

        }

        // end node -> draw way; select otherwise
        boolean isFirst = parentWay.firstNode().equals(dragNode);
        boolean isLast = parentWay.lastNode().equals(dragNode);
        if (isFirst == isLast)
            return new SelectedWayNode(parentWay, dragNode);
        else
            return new DrawWay(parentWay, isLast);
    }

    // FIXME void
    public ControllerState dragTo(int toX, int toY) {
        Command c = Main.main.undoRedo.commands.isEmpty() ? null : Main.main.undoRedo.commands.getLast();
        if (c instanceof SequenceCommand) {
            c = ((SequenceCommand) c).getLastCommand();
        }
        EastNorth firstEN = Main.map.mapView.getEastNorth(downX, downY);
        EastNorth currentEN = Main.map.mapView.getEastNorth(toX, toY);

        Beboj.debug("cur", toX+"x"+toY);

        if (c instanceof MoveCommand
                && Collections.singleton(dragNode).equals(((MoveCommand) c).getParticipatingPrimitives())
                && !needFreshMoveCommand) {
            ((MoveCommand) c).moveAgainTo(currentEN.east() - firstEN.east(), currentEN.north() - firstEN.north());
            Beboj.log("m");
        } else {
            c = new MoveCommand(dragNode, currentEN.east() - firstEN.east(), currentEN.north() - firstEN.north());
            Main.main.undoRedo.add(c);
            needFreshMoveCommand = false;
            Beboj.log("M");
        }

        Beboj.canvasView.repaint();

        return this;
    }

    @Override
    public void enterState(ControllerState oldState) {
        needFreshMoveCommand = true;
        enterTime = System.currentTimeMillis();
        dragstate = DragState.NOT_MOVED;
        previousState = oldState;
    }

    @Override
    public void exitState(ControllerState newState) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "DragWayNode";
    }

}
