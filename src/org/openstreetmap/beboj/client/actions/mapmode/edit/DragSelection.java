// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.actions.mapmode.edit;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

import java.util.Collection;

import org.openstreetmap.beboj.client.Beboj;
import org.openstreetmap.beboj.client.actions.mapmode.edit.DragMap.DragState;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.command.MoveCommand;
import org.openstreetmap.josm.command.SequenceCommand;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.visitor.AllNodesVisitor;

public class DragSelection extends ControllerState {
    private long enterTime;
    private int downX, downY;
    private DragState dragstate;

    private boolean needFreshMoveCommand;

    private boolean virgin = true;

    public DragSelection(int downX, int downY) {
        super();
        this.downX = downX;
        this.downY = downY;

        Beboj.debug("down", downX+"x"+downY);
    }

    @Override
    public ControllerState onMouseDown(MouseDownEvent evt, OsmPrimitive osm) {
        throw new UnsupportedOperationException();
//        return this; // FIXME throw?
    }

    @Override
    public ControllerState onMouseMove(MouseMoveEvent evt, OsmPrimitive osm) {
        if (dragstate == DragState.NOT_MOVED) {
            if ((Math.abs(downX - evt.getX()) < 3 && Math.abs(downY - evt.getY()) < 3) ||
                    System.currentTimeMillis() - enterTime < 300l)
                return this;
        }
        dragstate = DragState.DRAGGING;
        dragTo(evt.getX(), evt.getY());
        return this;
    }

    @Override
    public ControllerState onMouseUp(MouseUpEvent evt, OsmPrimitive osm) {
        // FIXME: return previous
        return findStateForSelection();
    }

    // FIXME void
    public ControllerState dragTo(int toX, int toY) {
        Command c = Main.main.undoRedo.commands.isEmpty() ? null : Main.main.undoRedo.commands.getLast();
        if (c instanceof SequenceCommand) {
            c = ((SequenceCommand) c).getLastCommand();
        }
        Collection<OsmPrimitive> selection = getCurrentDataSet().getSelectedNodesAndWays();
        Collection<Node> affectedNodes = AllNodesVisitor.getAllNodes(selection);


        EastNorth firstEN = Main.map.mapView.getEastNorth(downX, downY);
        EastNorth currentEN = Main.map.mapView.getEastNorth(toX, toY);

        Beboj.debug("cur", toX+"x"+toY);

        if (c instanceof MoveCommand && affectedNodes.equals(((MoveCommand) c).getParticipatingPrimitives()) && !needFreshMoveCommand) {
            ((MoveCommand) c).moveAgainTo(currentEN.east() - firstEN.east(), currentEN.north() - firstEN.north());
            Beboj.log("m");
        } else {
            c = new MoveCommand(selection, currentEN.east() - firstEN.east(), currentEN.north() - firstEN.north());
            Main.main.undoRedo.add(c);
            needFreshMoveCommand = false;
            Beboj.log("M");
        }

        Beboj.canvasView.repaint();

        return this;
    }

    @Override
    public void enterState(ControllerState oldState) {
        if (!virgin) throw new AssertionError();
        needFreshMoveCommand = true;
        enterTime = System.currentTimeMillis();
        dragstate = DragState.NOT_MOVED;
        previousState = oldState;

        // TODO Auto-generated method stub

    }

    @Override
    public void exitState(ControllerState newState) {
        virgin = false;

        Beboj.remove_debug("down");
        Beboj.remove_debug("cur");
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "DragSelection";
    }
}
