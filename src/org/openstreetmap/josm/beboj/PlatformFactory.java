// License: GPL. See LICENSE file for details.
package org.openstreetmap.josm.beboj;

import org.openstreetmap.josm.data.osm.visitor.paint.PaintVisitor;

/**
 * Factory for all kinds of platform dependent behavior.
 */
public interface PlatformFactory {

    PaintVisitor getDefaultPaintVisitor();

}
