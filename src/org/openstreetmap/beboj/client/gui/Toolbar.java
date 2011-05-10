// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

import org.openstreetmap.beboj.client.Beboj;
import org.openstreetmap.beboj.client.data.SampleDataSet;
import org.openstreetmap.beboj.client.imagery.OpenLayers;
import org.openstreetmap.beboj.client.io.API;
import org.openstreetmap.beboj.client.io.APIAsync;
import org.openstreetmap.beboj.shared.data.osm.SimpleDataSet;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.DiscreteZoomNavigationSupport;

public class Toolbar extends HorizontalPanel {

    public Toolbar () {
        Image i1 = new Image("images/download.png");
        Image i2 = new Image("images/upload.png");
        Image i3 = new Image("images/preference.png");
        Image i5 = new Image("images/imagery_menu.png");
        Image i7 = new Image("images2/zoom-in.png");
        Image i8 = new Image("images2/zoom-out.png");

        PushButton t1 = new PushButton(i1, new DownloadCommand());
        PushButton t1a = new PushButton("Load sample dataset", new SampleDataSetCommand());
        PushButton t2 = new PushButton(i2, new UploadCommand());
        t2.setEnabled(false);
        PushButton t3 = new PushButton(i3, new PrefCommand());
        t3.setEnabled(false);
        PushButton t4 = new PushButton("Repaint", new RepaintCommand());
        PushButton t5 = new PushButton(i5, new ImageryCommand());

        PushButton t6 = new PushButton("zoomToData", new ZoomToDataCommand());
        PushButton t7 = new PushButton(i7, new ZoomInCommand());
        PushButton t8 = new PushButton(i8, new ZoomOutCommand());

        add(t1);
        add(t1a);
        add(t2);
        add(t3);
        add(t4);
        add(t5);
        add(t6);
        add(t7);
        add(t8);
    }

    public class DownloadCommand implements ClickHandler  {
        @Override
        public void onClick(ClickEvent event) {
            APIAsync as = GWT.create(API.class);
            as.greetServer("hoho", new AsyncCallback<SimpleDataSet>() {

                @Override
                public void onSuccess(SimpleDataSet result) {
                    Window.alert("RPC success "+ result);

                }

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("RPC failure");
                }
            });
        }
    }

    public class SampleDataSetCommand implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            SampleDataSet.loadSampleDataSet();
            zoomToData();
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
            DiscreteZoomNavigationSupport nav = (DiscreteZoomNavigationSupport) Main.map.mapView.nav;
            Beboj.olmap = OpenLayers.newMap(nav.getZoom(), nav.getCenter().east(), nav.getCenter().north());
            GWT.log("res "+Beboj.olmap.getResolution());
        }
    }

    public class ZoomToDataCommand implements ClickHandler  {
        @Override
        public void onClick(ClickEvent event) {
            zoomToData();
        }
    }

    public void zoomToData() {
        BoundingXYVisitor bboxCalculator = new BoundingXYVisitor();
        Main.main.getEditLayer().visitBoundingBox(bboxCalculator);
        Main.map.mapView.recalculateCenterScale(bboxCalculator);
    }

    public class ZoomInCommand implements ClickHandler  {
        @Override
        public void onClick(ClickEvent event) {
            DiscreteZoomNavigationSupport nav = (DiscreteZoomNavigationSupport) Main.map.mapView.nav;
            nav.zoomIn();
        }
    }

    public class ZoomOutCommand implements ClickHandler  {
        @Override
        public void onClick(ClickEvent event) {
            DiscreteZoomNavigationSupport nav = (DiscreteZoomNavigationSupport) Main.map.mapView.nav;
            nav.zoomOut();
        }
    }
}
