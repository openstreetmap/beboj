// License: GPL. See LICENSE file for details.
package org.openstreetmap.josm.gui;

import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.Stack;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.beboj.CanvasView;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.ProjectionBounds;
import org.openstreetmap.josm.data.coor.CachedLatLon;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;

/**
 * GWT
 *
 * This class is not in the JOSM code base.
 */

/**
 * Add support for slippy map style zoom level (in contrast to JOSM's smooth zoom).
 */
public class DiscreteZoomNavigationSupport extends AbstractNavigationSupport {

    public PropertyChangeSupport propertyChangeManager = new PropertyChangeSupport(this);

    protected Integer zoom = 12;

    protected final static double FAC = 2.0 * Math.PI * 6378137.0 / 256.0;

    /**
     * The scale factor in x or y-units per pixel. This means, if scale = 10,
     * every physical pixel on screen are 10 x or 10 y units in the
     * northing/easting space of the projection.
     */
    public double getScale() {
        return zoomToScale(zoom);
    }

    protected double zoomToScale(int zoom) {
        if (zoom < 0)
            throw new AssertionError();
        return FAC / (1 << zoom);
    }


    protected final static double logFAC = Math.log(FAC);
    protected final static double log2 = Math.log(2.0);

    protected Integer scaleToZoom(Double s) {
        if (s == null)
            return null;
        int z = (int) Math.floor((logFAC - Math.log(s)) / log2);
        return Math.max(0, z);
    }

    public DiscreteZoomNavigationSupport(CanvasView view) {
        super(view);
    }

    @Override
    public boolean isReady() {
        return center != null && zoom >= 0;
    }

    public void zoomIn() {
        zoomTo(getCenter(), zoom + 1);
    }

    public void zoomOut() {
        if (zoom == 0)
            return;
        zoomTo(getCenter(), zoom - 1);
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        zoomToScale(zoom);
    }

    /**
     * Zoom to the given coordinate.
     * @param newCenter The center x-value (easting) to zoom to.
     * @param scale The scale to use.
     */
    private void zoomTo(EastNorth newCenter, int newZoom) {
        Bounds b = getProjection().getWorldBoundsLatLon();
        CachedLatLon cl = new CachedLatLon(newCenter);
        boolean changed = false;
        double lat = cl.lat();
        double lon = cl.lon();
        if(lat < b.getMin().lat()) {changed = true; lat = b.getMin().lat(); }
        else if(lat > b.getMax().lat()) {changed = true; lat = b.getMax().lat(); }
        if(lon < b.getMin().lon()) {changed = true; lon = b.getMin().lon(); }
        else if(lon > b.getMax().lon()) {changed = true; lon = b.getMax().lon(); }
        if(changed) {
            newCenter = new CachedLatLon(lat, lon).getEastNorth();
        }
        int width = view.getWidth()/2;
        int height = view.getHeight()/2;
        LatLon l1 = new LatLon(b.getMin().lat(), lon);
        LatLon l2 = new LatLon(b.getMax().lat(), lon);
        EastNorth e1 = getProjection().latlon2eastNorth(l1);
        EastNorth e2 = getProjection().latlon2eastNorth(l2);
        double d = e2.north() - e1.north();
        double newScale = zoomToScale(newZoom);
        if(d < height*newScale)
        {
            double newScaleH = d/height;
            e1 = getProjection().latlon2eastNorth(new LatLon(lat, b.getMin().lon()));
            e2 = getProjection().latlon2eastNorth(new LatLon(lat, b.getMax().lon()));
            d = e2.east() - e1.east();
            if(d < width*newScale) {
                newZoom = scaleToZoom(Math.max(newScaleH, d/width));
            }
        }
        else
        {
            d = d/(l1.greatCircleDistance(l2)*height*10);
            if(newScale < d) {
                newZoom = scaleToZoom(d);
            }
        }

        if (!newCenter.equals(center) || (zoom != newZoom)) {
            pushZoomUndo(center, zoom);
            zoomNoUndoTo(newCenter, newZoom);
        }
    }

    /**
     * Zoom to the given coordinate without adding to the zoom undo buffer.
     * @param newCenter The center x-value (easting) to zoom to.
     * @param scale The scale to use.
     */
    private void zoomNoUndoTo(EastNorth newCenter, int newZoom) {
        if (!newCenter.equals(center)) {
            EastNorth oldCenter = center;
            center = newCenter;
            propertyChangeManager.firePropertyChange("center", oldCenter, newCenter);
        }
        if (zoom != newZoom) {
            double oldZoom = zoom;
            zoom = newZoom;
            propertyChangeManager.firePropertyChange("scale", oldZoom, newZoom);
        }

        view.repaint();
        fireZoomChanged();
    }

    @Override
    public void zoomTo(EastNorth newCenter) {
        zoomTo(newCenter, zoom);
    }

    @Override
    public void zoomTo(LatLon newCenter) {
        if(newCenter instanceof CachedLatLon) {
            zoomTo(((CachedLatLon)newCenter).getEastNorth(), zoom);
        } else {
            zoomTo(getProjection().latlon2eastNorth(newCenter), zoom);
        }
    }

//    public void zoomToFactor(double x, double y, double factor) {
//        double newScale = getScale()*factor;
//        // New center position so that point under the mouse pointer stays the same place as it was before zooming
//        // You will get the formula by simplifying this expression: newCenter = oldCenter + mouseCoordinatesInNewZoom - mouseCoordinatesInOldZoom
//        zoomTo(new EastNorth(
//                center.east() - (x - view.getWidth()/2.0) * (newScale - getScale()),
//                center.north() + (y - view.getHeight()/2.0) * (newScale - getScale())),
//                newScale);
//    }

//    public void zoomToFactor(EastNorth newCenter, double factor) {
//        zoomTo(newCenter, scale*factor);
//    }
//
//    public void zoomToFactor(double factor) {
//        zoomTo(center, scale*factor);
//    }

    @Override
    public void zoomTo(ProjectionBounds box) {
        // -20 to leave some border
        int w = view.getWidth()-20;
        if (w < 20) {
            w = 20;
        }
        int h = view.getHeight()-20;
        if (h < 20) {
            h = 20;
        }

        double scaleX = (box.maxEast-box.minEast)/w;
        double scaleY = (box.maxNorth-box.minNorth)/h;
        int newZoom = scaleToZoom(Math.max(scaleX, scaleY));

        zoomTo(box.getCenter(), newZoom);
    }

    public void zoomTo(Bounds box) {
        zoomTo(new ProjectionBounds(getProjection().latlon2eastNorth(box.getMin()),
                getProjection().latlon2eastNorth(box.getMax())));
    }

    private class ZoomData {
        LatLon center;
        int zoom;

        public ZoomData(EastNorth center, int zoom) {
            this.center = new CachedLatLon(center);
            this.zoom = zoom;
        }

        public EastNorth getCenterEastNorth() {
            return getProjection().latlon2eastNorth(center);
        }

        public int getZoom() {
            return zoom;
        }
    }

    private Stack<ZoomData> zoomUndoBuffer = new Stack<ZoomData>();
    private Stack<ZoomData> zoomRedoBuffer = new Stack<ZoomData>();
    private Date zoomTimestamp = new Date();

    private void pushZoomUndo(EastNorth center, int zoom) {
        Date now = new Date();
        if ((now.getTime() - zoomTimestamp.getTime()) > (Main.pref.getDouble("zoom.undo.delay", 1.0) * 1000)) {
            zoomUndoBuffer.push(new ZoomData(center, zoom));
            if (zoomUndoBuffer.size() > Main.pref.getInteger("zoom.undo.max", 50)) {
                zoomUndoBuffer.remove(0);
            }
            zoomRedoBuffer.clear();
        }
        zoomTimestamp = now;
    }

    public void zoomPrevious() {
        if (!zoomUndoBuffer.isEmpty()) {
            ZoomData zd = zoomUndoBuffer.pop();
            zoomRedoBuffer.push(new ZoomData(center, zoom));
            zoomNoUndoTo(zd.getCenterEastNorth(), zoom);
        }
    }

    public void zoomNext() {
        if (!zoomRedoBuffer.isEmpty()) {
            ZoomData zd = zoomRedoBuffer.pop();
            zoomUndoBuffer.push(new ZoomData(center, zoom));
            zoomNoUndoTo(zd.getCenterEastNorth(), zd.getZoom());
        }
    }

    public boolean hasZoomUndoEntries() {
        return !zoomUndoBuffer.isEmpty();
    }

    public boolean hasZoomRedoEntries() {
        return !zoomRedoBuffer.isEmpty();
    }

}
