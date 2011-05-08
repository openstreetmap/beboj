// License: GPL. See LICENSE file for details.
package org.openstreetmap.josm.gui;

import java.util.concurrent.CopyOnWriteArrayList;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.beboj.CanvasView;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.ProjectionBounds;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.projection.Projection;

/**
 * GWT
 *
 * This class is not in the JOSM code base.
 */

/**
 * Abstract base class for different type of NavigationSupport.
 */
public abstract class AbstractNavigationSupport implements NavigationSupport {
    /**
     * Center n/e coordinate of the desired screen center.
     */
    protected EastNorth center = calculateDefaultCenter();

    protected CanvasView view;

    public AbstractNavigationSupport(CanvasView view) {
        this.view = view;
    }

    /**
     * the zoom listeners
     */
    private final CopyOnWriteArrayList<ZoomChangeListener> zoomChangeListeners = new CopyOnWriteArrayList<ZoomChangeListener>();

    /**
     * Removes a zoom change listener
     *
     * @param listener the listener. Ignored if null or already absent
     */
    public void removeZoomChangeListener(ZoomChangeListener listener) {
        zoomChangeListeners.remove(listener);
    }

    protected void fireZoomChanged() {
        for (ZoomChangeListener l : zoomChangeListeners) {
            l.zoomChanged();
        }
    }

    /**
     * Adds a zoom change listener
     *
     * @param listener the listener. Ignored if null or already registered.
     */
    public void addZoomChangeListener(ZoomChangeListener listener) {
        if (listener != null) {
            zoomChangeListeners.addIfAbsent(listener);
        }
    }

    /**
     * @return Returns the center point. A copy is returned, so users cannot
     *      change the center by accessing the return value. Use zoomTo instead.
     */
    public EastNorth getCenter() {
        return center;
    }


    @Override
    abstract public double getScale();

    @Override
    abstract public void zoomTo(EastNorth newCenter);

    @Override
    abstract public void zoomTo(ProjectionBounds box);

    @Override
    abstract public boolean isReady();

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
