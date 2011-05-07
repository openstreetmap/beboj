// License: GPL. See LICENSE file for details.
package org.openstreetmap.josm.gui;

import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.beboj.CanvasView;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.ProjectionBounds;
import org.openstreetmap.josm.data.coor.CachedLatLon;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.projection.Projection;

/**
 * GWT
 *
 * This class is not present in JOSM. The code was originally in NavigationonalComponent.java
 * and moved out to introduce other methods of zoom.
 *
 * notes
 *  zoomNoUndoTo does not trigger repaint
 */

public class SmoothZoomNavigationSupport implements NavigationSupport {

    public PropertyChangeSupport propertyChangeManager = new PropertyChangeSupport(this);

    /**
     * The scale factor in x or y-units per pixel. This means, if scale = 10,
     * every physical pixel on screen are 10 x or 10 y units in the
     * northing/easting space of the projection.
     */
    protected double scale = Main.proj.getDefaultZoomInPPD();
    /**
     * Center n/e coordinate of the desired screen center.
     */
    protected EastNorth center = calculateDefaultCenter();

    protected CanvasView view;

    public SmoothZoomNavigationSupport(CanvasView view) {
        this.view = view;
    }

    /**
     * @return Returns the center point. A copy is returned, so users cannot
     *      change the center by accessing the return value. Use zoomTo instead.
     */
    public EastNorth getCenter() {
        return center;
    }

    public double getScale() {
        return scale;
    }

    public boolean isReady() {
        return center != null && scale > 0;
    }

    /**
     * Interface to notify listeners of the change of the zoom area.
     */
    public interface ZoomChangeListener {
        void zoomChanged();
    }

    /**
     * the zoom listeners
     */
    private static final CopyOnWriteArrayList<ZoomChangeListener> zoomChangeListeners = new CopyOnWriteArrayList<ZoomChangeListener>();

    /**
     * Removes a zoom change listener
     *
     * @param listener the listener. Ignored if null or already absent
     */
    public static void removeZoomChangeListener(ZoomChangeListener listener) {
        zoomChangeListeners.remove(listener);
    }

    /**
     * Adds a zoom change listener
     *
     * @param listener the listener. Ignored if null or already registered.
     */
    public static void addZoomChangeListener(ZoomChangeListener listener) {
        if (listener != null) {
            zoomChangeListeners.addIfAbsent(listener);
        }
    }

    protected static void fireZoomChanged() {
        for (ZoomChangeListener l : zoomChangeListeners) {
            l.zoomChanged();
        }
    }

    /**
     * Zoom to the given coordinate.
     * @param newCenter The center x-value (easting) to zoom to.
     * @param scale The scale to use.
     */
    private void zoomTo(EastNorth newCenter, double newScale) {
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
        if(d < height*newScale)
        {
            double newScaleH = d/height;
            e1 = getProjection().latlon2eastNorth(new LatLon(lat, b.getMin().lon()));
            e2 = getProjection().latlon2eastNorth(new LatLon(lat, b.getMax().lon()));
            d = e2.east() - e1.east();
            if(d < width*newScale) {
                newScale = Math.max(newScaleH, d/width);
            }
        }
        else
        {
            d = d/(l1.greatCircleDistance(l2)*height*10);
            if(newScale < d) {
                newScale = d;
            }
        }

        if (!newCenter.equals(center) || (scale != newScale)) {
            pushZoomUndo(center, scale);
            zoomNoUndoTo(newCenter, newScale);
        }
    }
    /**
     * Zoom to the given coordinate without adding to the zoom undo buffer.
     * @param newCenter The center x-value (easting) to zoom to.
     * @param scale The scale to use.
     */
    private void zoomNoUndoTo(EastNorth newCenter, double newScale) {
        if (!newCenter.equals(center)) {
            EastNorth oldCenter = center;
            center = newCenter;
            propertyChangeManager.firePropertyChange("center", oldCenter, newCenter);
        }
        if (scale != newScale) {
            double oldScale = scale;
            scale = newScale;
            propertyChangeManager.firePropertyChange("scale", oldScale, newScale);
        }

        //view.repaint();
        fireZoomChanged();
    }

    public void zoomTo(EastNorth newCenter) {
        zoomTo(newCenter, scale);
    }

    public void zoomTo(LatLon newCenter) {
        if(newCenter instanceof CachedLatLon) {
            zoomTo(((CachedLatLon)newCenter).getEastNorth(), scale);
        } else {
            zoomTo(getProjection().latlon2eastNorth(newCenter), scale);
        }
    }

    public void zoomToFactor(double x, double y, double factor) {
        double newScale = scale*factor;
        // New center position so that point under the mouse pointer stays the same place as it was before zooming
        // You will get the formula by simplifying this expression: newCenter = oldCenter + mouseCoordinatesInNewZoom - mouseCoordinatesInOldZoom
        zoomTo(new EastNorth(
                center.east() - (x - view.getWidth()/2.0) * (newScale - scale),
                center.north() + (y - view.getHeight()/2.0) * (newScale - scale)),
                newScale);
    }

    public void zoomToFactor(EastNorth newCenter, double factor) {
        zoomTo(newCenter, scale*factor);
    }

    public void zoomToFactor(double factor) {
        zoomTo(center, scale*factor);
    }

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
        double newScale = Math.max(scaleX, scaleY);

        zoomTo(box.getCenter(), newScale);
    }

    public void zoomTo(Bounds box) {
        zoomTo(new ProjectionBounds(getProjection().latlon2eastNorth(box.getMin()),
                getProjection().latlon2eastNorth(box.getMax())));
    }

    private class ZoomData {
        LatLon center;
        double scale;

        public ZoomData(EastNorth center, double scale) {
            this.center = new CachedLatLon(center);
            this.scale = scale;
        }

        public EastNorth getCenterEastNorth() {
            return getProjection().latlon2eastNorth(center);
        }

        public double getScale() {
            return scale;
        }
    }

    private Stack<ZoomData> zoomUndoBuffer = new Stack<ZoomData>();
    private Stack<ZoomData> zoomRedoBuffer = new Stack<ZoomData>();
    private Date zoomTimestamp = new Date();

    private void pushZoomUndo(EastNorth center, double scale) {
        Date now = new Date();
        if ((now.getTime() - zoomTimestamp.getTime()) > (Main.pref.getDouble("zoom.undo.delay", 1.0) * 1000)) {
            zoomUndoBuffer.push(new ZoomData(center, scale));
            if (zoomUndoBuffer.size() > Main.pref.getInteger("zoom.undo.max", 50)) {
                zoomUndoBuffer.remove(0);
            }
            zoomRedoBuffer.clear();
        }
        zoomTimestamp = now;
    }

    public void zoomPrevious() {
        if (!zoomUndoBuffer.isEmpty()) {
            ZoomData zoom = zoomUndoBuffer.pop();
            zoomRedoBuffer.push(new ZoomData(center, scale));
            zoomNoUndoTo(zoom.getCenterEastNorth(), zoom.getScale());
        }
    }
    public void zoomNext() {
        if (!zoomRedoBuffer.isEmpty()) {
            ZoomData zoom = zoomRedoBuffer.pop();
            zoomUndoBuffer.push(new ZoomData(center, scale));
            zoomNoUndoTo(zoom.getCenterEastNorth(), zoom.getScale());
        }
    }

    public boolean hasZoomUndoEntries() {
        return !zoomUndoBuffer.isEmpty();
    }

    public boolean hasZoomRedoEntries() {
        return !zoomRedoBuffer.isEmpty();
    }
    private EastNorth calculateDefaultCenter() {
        Bounds b = Main.proj.getWorldBoundsLatLon();
        double lat = (b.getMax().lat() + b.getMin().lat())/2;
        double lon = (b.getMax().lon() + b.getMin().lon())/2;

        return Main.proj.latlon2eastNorth(new LatLon(lat, lon));
    }
    /**
     * @return The projection to be used in calculating stuff.
     */
    public Projection getProjection() {
        return Main.proj;
    }
}
