// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client;

import org.openstreetmap.beboj.client.gui.BebojPaintVisitor;
import org.openstreetmap.josm.beboj.PlatformFactory;
import org.openstreetmap.josm.data.osm.visitor.paint.PaintVisitor;

public class BebojPlatformFactory implements PlatformFactory {

    PaintVisitor paintVisitor;

    @Override
    public PaintVisitor getDefaultPaintVisitor() {
        if (paintVisitor == null) {
            paintVisitor = new BebojPaintVisitor();
        }
        return paintVisitor;
    }

}
