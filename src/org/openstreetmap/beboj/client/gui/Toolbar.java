// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

import org.openstreetmap.beboj.client.Beboj;
import org.openstreetmap.beboj.client.imagery.OpenLayers;
import org.openstreetmap.beboj.client.imagery.OpenLayers.OLMap;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;

public class Toolbar extends HorizontalPanel {

    public OLMap map;

    public Toolbar () {
        Image i1 = new Image("images/download.png");
        i1.setTitle("Load sample dataset");
        Image i2 = new Image("images/upload.png");
        Image i3 = new Image("images/preference.png");
        Image i5 = new Image("images/imagery_menu.png");

        PushButton t1 = new PushButton(i1, new DownloadCommand());
        PushButton t2 = new PushButton(i2, new UploadCommand());
        t2.setEnabled(false);
        PushButton t3 = new PushButton(i3, new PrefCommand());
        t3.setEnabled(false);
        PushButton t4 = new PushButton("Repaint", new RepaintCommand());
        PushButton t5 = new PushButton(i5, new ImageryCommand());

        add(t1);
        add(t2);
        add(t3);
        add(t4);
        add(t5);
    }

    private void loadSampleDataSet() {
        OsmDataLayer l = Main.main.getEditLayer();
        Node n1 = new Node(new LatLon(1.0, 2.0));
        Node n2 = new Node(new LatLon(2.0, 3.0));
        Way w1 = new Way();
        w1.addNode(n1);
        w1.addNode(n2);
        for (OsmPrimitive osm : new OsmPrimitive[] {n1, n2, w1}) {
            l.data.addPrimitive(osm);
        }
    }

    public class DownloadCommand implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            loadSampleDataSet();
            Beboj.canvasView.repaint();
        }
    }

    public class UploadCommand implements ClickHandler  {
        @Override
        public void onClick(ClickEvent event) {
        }
    }

    public class PrefCommand implements ClickHandler  {
        @Override
        public void onClick(ClickEvent event) {
        }
    }

    public class RepaintCommand implements ClickHandler  {
        @Override
        public void onClick(ClickEvent event) {
            Beboj.canvasView.repaint();
        }
    }

    public class ImageryCommand implements ClickHandler  {
        @Override
        public void onClick(ClickEvent event) {
            map = OpenLayers.newMap();
            GWT.log("res "+map.getResolution());
        }
    }
}
