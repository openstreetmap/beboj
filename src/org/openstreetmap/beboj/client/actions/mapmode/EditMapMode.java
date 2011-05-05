// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.actions.mapmode;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Collection;

import org.openstreetmap.beboj.client.Beboj;
import org.openstreetmap.beboj.client.actions.mapmode.edit.ControllerState;
import org.openstreetmap.beboj.client.actions.mapmode.edit.NoSelection;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.mapmode.MapMode;
import org.openstreetmap.josm.data.SelectionChangedListener;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.OsmPrimitive;

public class EditMapMode extends MapMode implements MouseDownHandler, MouseMoveHandler, MouseUpHandler, SelectionChangedListener {

    protected HandlerRegistration reg1, reg2, reg3;

    public ControllerState state;

    @Override
    public void enterMode() {
        super.enterMode();
        DataSet.addSelectionListener(this);
        reg1 = Beboj.canv.addMouseDownHandler(this);
        reg2 = Beboj.canv.addMouseMoveHandler(this);
        reg3 = Beboj.canv.addMouseUpHandler(this);
        setState(new NoSelection());
    }

    @Override
    public void exitMode() {
        reg3.removeHandler();
        reg2.removeHandler();
        reg1.removeHandler();
        DataSet.removeSelectionListener(this);
        Beboj.remove_debug("state");
        super.exitMode();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // FIXME
        super.actionPerformed(e);
    }

    @Override
    public String getImageUrl() {
        return "images2/edit.png";
    }

    @Override
    public void selectionChanged(Collection<? extends OsmPrimitive> newSelection) {
        Beboj.canvasView.repaint();
    }

    public void setState(ControllerState newState) {
        if (newState == state)
            return;
        if (state != null) {
            state.exitState(newState);
        }
//        newState.setController(this);
//        newState.setPreviousState(state);
        ControllerState previousState = state;
        state = newState;
        state.enterState(previousState);

        Beboj.debug("state", state.toString());
    }

    @Override
    public void onMouseUp(MouseUpEvent evt) {
        OsmPrimitive hit = getHitPrimitive(evt.getX(), evt.getY());
        ControllerState newState = state.onMouseUp(evt, hit);
        setState(newState);
        evt.preventDefault(); // prevents strange effects, e.g. Chrome selects the
        // whole area when double clicking on the canvas
    }

    @Override
    public void onMouseMove(MouseMoveEvent evt) {
        OsmPrimitive hit = getHitPrimitive(evt.getX(), evt.getY());
        ControllerState newState = state.onMouseMove(evt, hit);
        setState(newState);
        evt.preventDefault();
    }

    @Override
    public void onMouseDown(MouseDownEvent evt) {
        OsmPrimitive hit = getHitPrimitive(evt.getX(), evt.getY());
        ControllerState newState = state.onMouseDown(evt, hit);
        setState(newState);
        evt.preventDefault();
    }

    private OsmPrimitive getHitPrimitive(int x, int y) {
        return Main.map.mapView.getNearestNodeOrWay(new Point(x, y), OsmPrimitive.isSelectablePredicate, false);
    }

}
