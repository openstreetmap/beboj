// License: GPL or Openlayers' BSD-style license. For details, see LICENSE file.
package org.openstreetmap.beboj.client.imagery;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class OpenLayers {

    public static native OLMap newMap() /*-{
        var apiKey = "AuMLnyuPu8t30kojQXLFxfpDJUvrg1d2vBsVZ-p07AZytezgQDQZ-ZMtuszAI-1i";

        var switcher = new $wnd.OpenLayers.Control.LayerSwitcher({
            div: $doc.getElementById('layers')
        });

        var map = new $wnd.OpenLayers.Map('map', {
            controls: [
                switcher,
                new $wnd.OpenLayers.Control.Attribution({
                    div: $doc.getElementById('attribution')
                })
            ]
        });

        switcher.minimizeControl();
        switcher.baseLbl.innerHTML = "Imagery";

        var empty = new $wnd.OpenLayers.Layer('No Background', {
            isBaseLayer: true,
            'displayInLayerSwitcher': true
        });

        var osm = new $wnd.OpenLayers.Layer.OSM('OSM (Mapnik)');

        var bing = new $wnd.OpenLayers.Layer.Bing({
            key: apiKey,
            type: "Aerial",
            name: "Bing"
        });

        var bingd = new $wnd.OpenLayers.Layer.Bing({
            key: apiKey,
            type: "Aerial",
            name: "Bing (dimmed)"
        });
        bingd.setOpacity(0.3);

        map.addLayers([empty, osm, bing, bingd]);
        map.setBaseLayer(bingd);

        map.setCenter(new $wnd.OpenLayers.LonLat(-71.147, 42.472).transform(
            new $wnd.OpenLayers.Projection("EPSG:4326"),
            map.getProjectionObject()
        ), 12);

        return map;
    }-*/;

    public static class OLMap extends JavaScriptObject {
        protected OLMap() {}

        public final native double getResolution() /*-{ return this.getResolution(); }-*/;

        public final native JsArray<OLLayer> getLayers() /*-{ return this.layers; }-*/;

        public final native void zoomTo(int zoom) /*-{ this.zoomTo(zoom); }-*/;

        public final native void zoomIn() /*-{ this.zoomIn(); }-*/;

        public final native void zoomOut() /*-{ this.zoomOut(); }-*/;

        public final native int getZoom() /*-{ return this.getZoom(); }-*/;

        public final native double getWidth() /*-{ return this.getSize().w; }-*/;

        public final native double getHeight() /*-{ return this.getSize().h; }-*/;

        public final native void setCenterAndZoom(double east, double north, int zoom) /*-{
            this.setCenter(new $wnd.OpenLayers.LonLat(east, north), zoom);
        }-*/;
    }

    public static class OLLayer extends JavaScriptObject {
        protected OLLayer() {}

        public final native void setOpacity(float o) /*-{ this.setOpacity(o); }-*/;
    }

}
