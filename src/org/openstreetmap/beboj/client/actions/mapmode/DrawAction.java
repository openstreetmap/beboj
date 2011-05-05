// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.actions.mapmode;

import static org.openstreetmap.josm.tools.I18n.tr;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.openstreetmap.beboj.client.Beboj;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.mapmode.MapMode;
import org.openstreetmap.josm.command.AddCommand;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.command.SequenceCommand;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;

public class DrawAction extends MapMode implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

    HandlerRegistration reg1, reg2, reg3;

    @Override
    public void enterMode() {
        super.enterMode();
        reg1 = Beboj.canv.addMouseDownHandler(this);
        reg2 = Beboj.canv.addMouseMoveHandler(this);
        reg3 = Beboj.canv.addMouseUpHandler(this);
        Beboj.canv.getElement().getStyle().setCursor(Cursor.CROSSHAIR);
    }

    @Override
    public void exitMode() {
        reg1.removeHandler();
        reg2.removeHandler();
        reg3.removeHandler();
        Beboj.canv.getElement().getStyle().clearCursor();
        super.enterMode();
    }

    @Override
    public void onMouseDown(MouseDownEvent e) {
        e.preventDefault();
    }

    @Override
    public void onMouseMove(MouseMoveEvent e) {
        e.preventDefault();
    }

    @Override
    public void onMouseUp(MouseUpEvent e) {

        Point mousePos = new Point(e.getX(), e.getY());

        DataSet ds = getCurrentDataSet();
        Collection<OsmPrimitive> selection = new ArrayList<OsmPrimitive>(ds.getSelected());
        Collection<Command> cmds = new LinkedList<Command>();
        Collection<OsmPrimitive> newSelection = new LinkedList<OsmPrimitive>(ds.getSelected());
        Node n = null;
        n = Main.map.mapView.getNearestNode(mousePos, OsmPrimitive.isSelectablePredicate);
        if (n != null) {
            Beboj.log("|");
        } else {
            n = new Node(Main.map.mapView.getLatLon(e.getX(), e.getY()));
            if (n.getCoor().isOutSideWorld()) {
//              JOptionPane.showMessageDialog(
//                      Main.parent,
//                      tr("Cannot add a node outside of the world."),
//                      tr("Warning"),
//                      JOptionPane.WARNING_MESSAGE
//              );
                return;
            }
            cmds.add(new AddCommand(n));
            String title = tr("Add node");
            Command c = new SequenceCommand(title, cmds);
            Main.main.undoRedo.add(c);
            Beboj.canvasView.repaint();
        }
        e.preventDefault();
    }

    @Override
    public String getImageUrl() {
        return "images2/add-node.png";
    }

}
