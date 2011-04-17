// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import org.openstreetmap.beboj.client.Beboj;
import org.openstreetmap.josm.data.coor.LatLon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

public class Toolbar extends HorizontalPanel {

    public Toolbar () {
        Image i1 = new Image("images/download.png");
        Image i2 = new Image("images/upload.png");
        Image i3 = new Image("images/preference.png");

        PushButton t1 = new PushButton(i1, new DownloadCommand());
        PushButton t2 = new PushButton(i2, new UploadCommand());
        PushButton t3 = new PushButton(i3, new PrefCommand());

        add(t1);
        add(t2);
        add(t3);
    }

    public class DownloadCommand implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            Beboj.canv.setPixelSize(200, 400);
            GWT.log("canvas resize");
        }
    }

    public class UploadCommand implements ClickHandler  {
        @Override
        public void onClick(ClickEvent event) {
            // basic test
            LatLon ll = new LatLon(1.2, 2.3);
            GWT.log("ll:"+ll.getX());
        }
    }

    public class PrefCommand implements ClickHandler  {
        @Override
        public void onClick(ClickEvent event) {
            GWT.log("this is a log message from Pref");
        }
    }
}
