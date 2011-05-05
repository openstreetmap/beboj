// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.actions.mapmode.edit;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

import java.awt.Graphics2D;
import java.awt.Point;

import org.openstreetmap.beboj.client.Beboj;
import org.openstreetmap.beboj.client.gui.CanvasGraphics2D;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.command.AddCommand;
import org.openstreetmap.josm.command.ChangeCommand;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.layer.MapViewPaintable;

public class DrawWay extends ControllerState implements MapViewPaintable {

    private final static long DOUBLE_CLICK_LIMIT = 1000l;
    private Node startNode;
    private Node lastNode;
    private Way way;
    private long lastClickTime;
    private Point lastMousePos;

    public DrawWay(Node startNode) {
        super();
        this.startNode = this.lastNode = startNode;
        way = new Way();
        way.addNode(startNode);
    }

    public DrawWay(Way way, boolean editEnd) {
        this.way = way;
        this.startNode = this.lastNode = editEnd ? way.lastNode() : way.firstNode();
    }

    @Override
    public ControllerState onMouseDown(MouseDownEvent evt, OsmPrimitive osm) {
        // TODO Auto-generated method stub
        // FIXME: drag
        return this;
    }

    @Override
    public ControllerState onMouseMove(MouseMoveEvent evt, OsmPrimitive osm) {
        lastMousePos = new Point(evt.getX(), evt.getY());
        Beboj.canvasView.repaint();
        return this;
    }

    @Override
    public ControllerState onMouseUp(MouseUpEvent evt, OsmPrimitive osm) {
        if (osm == null) { // create new node and insert it into way

            Node newNode = new Node(Main.map.mapView.getLatLon(evt.getX(), evt.getY()));
            Main.main.undoRedo.add(new AddCommand(newNode));
            if (way.getNodesCount() == 1) {
                // haven't added way yet
                Main.main.undoRedo.add(new AddCommand(way));
                getCurrentDataSet().setSelected(way);
            }
            Way newWay = new Way(way);
            newWay.addNode(newNode);
            Main.main.undoRedo.add(new ChangeCommand(way, newWay));
            lastNode = newNode;
            Beboj.canvasView.repaint();
        } if (osm instanceof Node) {
            if (System.currentTimeMillis() - lastClickTime < DOUBLE_CLICK_LIMIT) {
                if (way.getNodesCount() == 1 && numWayReferrer(way.getNode(0)) == 1 && osm.equals(startNode)) {
                    // Actually the user double-clicked to make a new node, they didn't want to draw a way at all.
                    return new SelectedPOINode((Node) osm);
                } else if (way.getNodesCount() == 1) {
                    // It's not a poi, but they've double-clicked or clicked-twice the first node - do nothing
                    return this;
                } else {
                    // double-click at end of way
//                    getCurrentDataSet().setSelected(way);
                    return new SelectedWay(way);
                }
            } else if (osm.equals(lastNode)) {
                // clicked slowly on the end node - do nothing
                return this;
            } else {
                // hit a node, add it to this way and carry on
                Way newWay = new Way(way);
                newWay.addNode((Node) osm);
                Main.main.undoRedo.add(new ChangeCommand(way, newWay));
                Beboj.canvasView.repaint();
            }
        }
        lastClickTime = System.currentTimeMillis();
        return this;
    }

    protected int numWayReferrer(Node n) {
        int i = 0;
        for (OsmPrimitive parent : n.getReferrers()) {
            if (parent instanceof Way) {
                ++i;
            }
        }
        if (way.getNodesCount() == 1) {
            // haven't added way to dataset, so it isn't reported as referrer
            if (way.getNode(0).equals(n)) {
                ++i;
            }
        }
        return i;
    }

    @Override
    public void enterState(ControllerState oldState) {
        lastClickTime = System.currentTimeMillis();
        Main.map.mapView.addTemporaryLayer(this);

        // TODO Auto-generated method stub
    }

    @Override
    public void exitState(ControllerState newState) {
        Main.map.mapView.removeTemporaryLayer(this);
        // TODO Auto-generated method stub
    }

    @Override
    public String toString() {
        return "DrawWay";
    }

    @Override
    public void paint(Graphics2D g, MapView mv, Bounds bbox) {
        Context2d c = ((CanvasGraphics2D) g).getContext2d();

        // don't draw line if we don't know where to
        if (lastMousePos == null) return;

        // don't draw line if mouse is outside window
        if (!Main.map.mapView.view.getBounds().contains(lastMousePos)) return;

        Point p1 = mv.getPoint(lastNode);
        Point p2 = lastMousePos;

        c.setStrokeStyle("#ffff00");
        c.beginPath();
        c.moveTo(p1.x, p1.y);
        c.lineTo(p2.x, p2.y);
        c.stroke();
    }

}
