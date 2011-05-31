// License: GPL. See LICENSE file for details.
package org.openstreetmap.josm.beboj;

import java.awt.Graphics2D;
import java.util.List;

import org.openstreetmap.josm.actions.mapmode.MapMode;
import org.openstreetmap.josm.data.osm.visitor.paint.Rendering;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.NavigationSupport;

/**
 * Factory for all kinds of platform dependent behavior.
 */
public interface PlatformFactory {

    Rendering createActiveRenderer(Graphics2D g, MapView mv, boolean inactive);

    List<MapMode> getMapModes();

    NavigationSupport getNavigationSupport(CanvasView view);

}
