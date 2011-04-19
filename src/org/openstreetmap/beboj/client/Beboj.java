// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client;

import org.openstreetmap.beboj.client.gui.MainUI;
import org.openstreetmap.josm.Main;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class Beboj implements EntryPoint {

    public static Canvas canv;

    /**
     * Entry point method.
     */
    public void onModuleLoad() {
        Main.init();

        MainUI ui = new MainUI();
        ui.getElement().setId("content-inner");

        RootPanel.get("content").add(ui);
    }
}
