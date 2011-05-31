// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

import org.openstreetmap.beboj.client.actions.mapmode.DrawAction;
import org.openstreetmap.beboj.client.actions.mapmode.EditMapMode;
import org.openstreetmap.beboj.client.actions.mapmode.PanAction;
import org.openstreetmap.beboj.client.gui.BebojPaintVisitor;
import org.openstreetmap.josm.actions.mapmode.MapMode;
import org.openstreetmap.josm.beboj.CanvasView;
import org.openstreetmap.josm.beboj.PlatformFactory;
import org.openstreetmap.josm.data.osm.visitor.paint.Rendering;
import org.openstreetmap.josm.gui.DiscreteZoomNavigationSupport;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.NavigationSupport;

public class BebojPlatformFactory implements PlatformFactory {

    protected BebojPaintVisitor paintVisitor;
    protected List<MapMode> modes;

    @Override
    public Rendering createActiveRenderer(Graphics2D g, MapView mv, boolean inactive) {
        if (paintVisitor == null) {
            paintVisitor = new BebojPaintVisitor();
        }
        paintVisitor.setGraphics(g);
        paintVisitor.setNavigatableComponent(mv);
        paintVisitor.setInactive(inactive);

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

    @Override
    public NavigationSupport getNavigationSupport(CanvasView view) {
        return new DiscreteZoomNavigationSupport(view);
    }
}
