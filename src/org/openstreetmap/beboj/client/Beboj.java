// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client;

import java.util.HashMap;
import java.util.Map;

import org.openstreetmap.beboj.client.gui.MainUI;
import org.openstreetmap.beboj.client.imagery.OpenLayers.OLMap;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.beboj.CanvasView;
import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.data.UndoRedoHandler;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.projection.Mercator;
import org.openstreetmap.josm.gui.DiscreteZoomNavigationSupport;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.gui.NavigationSupport.ZoomChangeListener;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Beboj implements EntryPoint {

    public static Canvas canv;

    public static CanvasView canvasView;

    public static OLMap olmap;

    /**
     * Entry point method.
     */
    public void onModuleLoad() {

        Main.platformFactory = new BebojPlatformFactory();
        Main.main = new Main();
        Main.pref = new Preferences();
        Main.proj = new Mercator();
        Main.main.undoRedo = new UndoRedoHandler();

        MainUI ui = new MainUI();
        ui.getElement().setId("content-inner");

        canv = ui.canv;
        canvasView = ui.canvView;
        Main.map = new MapFrame(canvasView);
        canvasView.setPresenter(Main.map.mapView);

        ui.setMapModesController(Main.map);

        final DiscreteZoomNavigationSupport nav = (DiscreteZoomNavigationSupport) Main.map.mapView.nav;
        nav.zoomTo(new LatLon(51.1254062,1.3148010));
        nav.setZoom(14);

        Main.main.addLayer(new OsmDataLayer(new DataSet(), OsmDataLayer.createNewName(), null));

        Main.map.mapView.nav.addZoomChangeListener(new ZoomChangeListener() {
            @Override
            public void zoomChanged() {
                syncOLMap();
                debug("zoom", nav.getZoom()+"");
            }
        });

        configureDebugElements();

        RootPanel.get("content").add(ui);

        ui.leftButtons.buttons[0].onClick();
        ui.leftButtons.buttons[0].setDown(true);
    }

    /**
     * Zoom the openlayers background map, such that it fits with the data.
     *
     * Note that both elements can have different size due to problems when
     * resizing the canvas dynamically.
     *
     * But it is assumed, that both elements have the same upper left corner.
     *
     * FIXME: remove canvas border, so this is true;
     *
     */
    public static void syncOLMap() {
        if (olmap == null)
            return;
        int width = (int) olmap.getWidth();
        int height =  (int) olmap.getHeight();
        EastNorth center = Main.map.mapView.getEastNorth(width / 2, height / 2);
        int zoom = ((DiscreteZoomNavigationSupport) Main.map.mapView.nav).getZoom();

        olmap.setCenterAndZoom(center.east(), center.north(), zoom);
    }

    /*****************
     * Debugging code
     *****************/

    public static ToggleButton tb;

    private void configureDebugElements() {
        tb = new ToggleButton("Enable exteded debug log");
        RootPanel.get("left").add(tb);

        PushButton pb = new PushButton("Clear quick log");
        pb.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Element log = RootPanel.get("log").getElement();
                log.setInnerText("");
            }
        });
        RootPanel.get("left").add(pb);
    }

    public static void log(String s) {
        Element log = RootPanel.get("log").getElement();
        String txt = log.getInnerText();
        if (log.getClientWidth() > 800)
            txt = "";
        log.setInnerText(txt+s);
    }

    private static VerticalPanel debugPanel;

    private static Map<String, HTML> debugSlots;

    public static void debug(String id, String value) {
        if (debugPanel == null) {
            Element left = RootPanel.get("left").getElement();
            debugPanel = new VerticalPanel();
            left.appendChild(debugPanel.getElement());
            debugSlots = new HashMap<String, HTML>();
        }
        HTML e = debugSlots.get(id);
        if (e == null) {
            e = new HTML("ho");
            e.getElement().setId(id);
            debugSlots.put(id, e);
            debugPanel.add(e);
        }
        e.getElement().setInnerText(escapeHtml(id+": "+value));
    }

    public static void remove_debug(String id) {
        HTML e = debugSlots.get(id);
        if (e != null) {
            debugPanel.remove(e);
            debugSlots.remove(id);
        }
    }

    private static String escapeHtml(String maybeHtml) {
        final Element div = DOM.createDiv();
        DOM.setInnerText(div, maybeHtml);
        return DOM.getInnerHTML(div);
    }
}
