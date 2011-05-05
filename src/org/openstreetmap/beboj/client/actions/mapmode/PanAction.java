// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.actions.mapmode;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

import org.openstreetmap.beboj.client.Beboj;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.mapmode.MapMode;
import org.openstreetmap.josm.data.coor.EastNorth;

public class PanAction extends MapMode implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

    boolean moving;
    EastNorth mousePosMove;
    HandlerRegistration reg1, reg2, reg3;

    @Override
    public void enterMode() {
        super.enterMode();
        reg1 = Beboj.canv.addMouseDownHandler(this);
        reg2 = Beboj.canv.addMouseMoveHandler(this);
        reg3 = Beboj.canv.addMouseUpHandler(this);
        moving = false;
    }

    @Override
    public void exitMode() {
        reg1.removeHandler();
        reg2.removeHandler();
        reg3.removeHandler();
        super.exitMode();
    }

    @Override
    public void onMouseMove(MouseMoveEvent e) {
        if (!moving)
            return;
        if (mousePosMove == null) {
            GWT.log(".");
        }
        EastNorth center = Main.map.mapView.getCenter();
        EastNorth mouseCenter = Main.map.mapView.getEastNorth(e.getX(), e.getY());
        Main.map.mapView.zoomTo(new EastNorth(
                mousePosMove.east() + center.east() - mouseCenter.east(),
                mousePosMove.north() + center.north() - mouseCenter.north()));
        Beboj.canvasView.repaint();
    }

    @Override
    public void onMouseDown(MouseDownEvent e) {
        mousePosMove = Main.map.mapView.getEastNorth(e.getX(), e.getY());
        moving = true;
        DOM.setCapture(Beboj.canv.getElement());
        // TODO
        // to really change the cursor globally (when it is dragged out of the canvas),
        // we may have to put some invisible
        // div on top of all, because the buttons have their own cursor property set
        // and this has precedence over the body cursor
        // 
        // alternative: use css hint "!important" (?)
        RootPanel.get().getElement().getStyle().setCursor(Cursor.MOVE);
        e.preventDefault();
    }

    @Override
    public void onMouseUp(MouseUpEvent e) {
        mousePosMove = null;
        DOM.releaseCapture(Beboj.canv.getElement());
        RootPanel.get().getElement().getStyle().clearCursor();
        e.preventDefault();
        moving = false;
    }

    @Override
    public String getImageUrl() {
        return "images2/move.png";
    }
}
