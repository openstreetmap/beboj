// License: GPL. See LICENSE file for details.
package org.openstreetmap.josm.gui;

import org.openstreetmap.josm.data.ProjectionBounds;
import org.openstreetmap.josm.data.coor.EastNorth;

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
}
