// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;

public class MainMenu extends MenuBar {
    public MainMenu() {
        Command c = new Command() {
            public void execute() {
                Window.alert("Test menu item!");
            }
        };

        MenuBar fileMenu = new MenuBar(true);
        fileMenu.addItem("Update selection", c);
        fileMenu.addItem("Update data", c);
        fileMenu.addItem("Download Primitive...", c);

        MenuBar editMenu = new MenuBar(true);
        editMenu.addItem("Undo", c);
        editMenu.addItem("Redo", c);
        editMenu.addItem("Select all", c);

        MenuBar toolsMenu = new MenuBar(true);
        toolsMenu.addItem("Combine Ways", c);
        toolsMenu.addItem("Flip Way", c);
        toolsMenu.addItem("Align Way", c);

        // Make a new menu bar, adding a few cascading menus to it.
        addItem("File", fileMenu);
        addItem("Edit", editMenu);
        addItem("Tools", toolsMenu);
        getElement().setId("menu");
    }
}
