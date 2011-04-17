// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import org.openstreetmap.beboj.client.gui.MainUI;

public class Beboj implements EntryPoint {

    /**
     * Entry point method.
     */
    public void onModuleLoad() {
        MainUI ui = new MainUI();
        ui.getElement().setId("content-inner");

        RootPanel.get("content").add(ui);
    }
}
