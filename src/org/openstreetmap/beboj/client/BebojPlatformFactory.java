// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client;

import java.util.Arrays;
import java.util.List;

import org.openstreetmap.beboj.client.actions.mapmode.DrawAction;
import org.openstreetmap.beboj.client.actions.mapmode.PanAction;
import org.openstreetmap.beboj.client.gui.BebojPaintVisitor;
import org.openstreetmap.beboj.client.actions.mapmode.EditMapMode;
import org.openstreetmap.josm.actions.mapmode.MapMode;
import org.openstreetmap.josm.beboj.PlatformFactory;
import org.openstreetmap.josm.data.osm.visitor.paint.PaintVisitor;

public class BebojPlatformFactory implements PlatformFactory {

    protected PaintVisitor paintVisitor;
    protected List<MapMode> modes;

    @Override
    public PaintVisitor getDefaultPaintVisitor() {
        if (paintVisitor == null) {
            paintVisitor = new BebojPaintVisitor();
        }
        return paintVisitor;
    }

    @Override
    public List<MapMode> getMapModes() {
        if (modes == null) {
            modes = Arrays.asList(new MapMode[] {
                    new EditMapMode(),
                    new PanAction(),
                    new DrawAction()
            });
        }
        return modes;
    }

}
