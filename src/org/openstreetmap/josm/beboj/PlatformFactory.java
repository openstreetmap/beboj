// License: GPL. See LICENSE file for details.
package org.openstreetmap.josm.beboj;

import java.util.List;

import org.openstreetmap.josm.actions.mapmode.MapMode;
import org.openstreetmap.josm.data.osm.visitor.paint.PaintVisitor;
import org.openstreetmap.josm.gui.NavigationSupport;

/**
 * Factory for all kinds of platform dependent behavior.
 */
public interface PlatformFactory {

    PaintVisitor getDefaultPaintVisitor();

    List<MapMode> getMapModes();

    NavigationSupport getNavigationSupport(CanvasView view);

}
