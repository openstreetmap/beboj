// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MainUI extends Composite {
    interface Binder extends UiBinder<Widget, MainUI> {}
    private static Binder uiBinder = GWT.create(Binder.class);

    @UiField
    public MainMenu menu;

    @UiField
    public Toolbar toolbar;

    @UiField
    public LeftButtonRow leftButtons;

    @UiField(provided=true)
    public Canvas canv;

    public MainUI() {
        canv = Canvas.createIfSupported();
        canv.getElement().setId("canvas");
        canv.setWidth("400");
        canv.setHeight("300");
        initWidget(uiBinder.createAndBindUi(this));
    }
}
