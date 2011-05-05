// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import org.openstreetmap.beboj.client.Beboj;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.osm.BBox;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.data.osm.visitor.paint.PaintVisitor;
import org.openstreetmap.josm.gui.NavigatableComponent;

public class BebojPaintVisitor implements PaintVisitor {
    /**
     * The environment to paint to.
     */
    protected Graphics2D g;

    protected Context2d c;

    /**
     * MapView to get screen coordinates.
     */
    protected NavigatableComponent nc;

    public boolean inactive;

    @Override
    public void setGraphics(Graphics2D g) {
        this.g = g;
    }

    @Override
    public void setNavigatableComponent(NavigatableComponent nc) {
        this.nc = nc;
    }

    @Override
    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    static int d = 0;
    @Override
    public void visitAll(DataSet data, boolean virtual, Bounds bounds) {
        if (Beboj.tb.isDown()) {
            GWT.log(" - visitAll - ");
        }
        String[] cs = new String[] { ".", "o", "O", "Q"};
        Beboj.log(cs[d]);
        if (++d > 3) d=0;

        BBox bbox = new BBox(bounds);

        c = ((CanvasGraphics2D) g).getContext2d();

        c.clearRect(0, 0, Beboj.canv.getCoordinateSpaceWidth(), Beboj.canv.getCoordinateSpaceHeight());
        // alternative way to clear the canvas:
        // Beboj.canv.setCoordinateSpaceWidth(Beboj.canv.getCoordinateSpaceWidth());

        List<Way> ways = data.searchWays(bbox);
        for (final Way w : ways) {
            visit(w);
        }


        for (final Node n: data.searchNodes(bbox)) {
            visit(n);
        }
    }

    public void visit(Node n) {
        Point p = nc.getPoint(n);
        int r = 3;
        c.setStrokeStyle(n.isSelected() ? "#ff0000" : "#000000");
        c.strokeRect(p.x - r, p.y - r, 2 * r, 2 * r);
    }

    public void visit(Way w) {
        if (w.isIncomplete() || w.getNodesCount() < 2)
            return;
        c.setStrokeStyle(w.isSelected() ? "#ff0000" : "#000000");
        c.beginPath();

        Iterator<Node> it = w.getNodes().iterator();
        if (it.hasNext()) {
            Point lastP = nc.getPoint(it.next());
            c.moveTo(lastP.x, lastP.y);

            for (int orderNumber = 1; it.hasNext(); orderNumber++) {
                Point p = nc.getPoint(it.next());
                c.lineTo(p.x, p.y);
                lastP = p;
            }
            c.stroke();
        }
    }
}
