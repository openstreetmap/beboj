// License: GPL. See LICENSE file for details.
package org.openstreetmap.josm.gui;

import org.openstreetmap.josm.data.ProjectionBounds;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;

/**
 * GWT
 *
 * This inteface is not present in JOSM.
 * See <code>SmoothZoomNavigationSupport</code> for details.
 */

public interface NavigationSupport {

    EastNorth getCenter();

    double getScale();

    void zoomTo(EastNorth newCenter);

    void zoomTo(ProjectionBounds box);

    boolean isReady();

    /**
     * Interface to notify listeners of the change of the zoom area.
     */
    public interface ZoomChangeListener {
        void zoomChanged();
    }

    void addZoomChangeListener(ZoomChangeListener listener);

    void removeZoomChangeListener(ZoomChangeListener listener);

    void zoomTo(LatLon newCenter);

}
