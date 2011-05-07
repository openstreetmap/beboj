// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.openstreetmap.josm.beboj.CanvasPresenter;
import org.openstreetmap.josm.beboj.CanvasView;
import org.openstreetmap.josm.gui.MapFrame;

public class MainUI extends Composite {
    interface Binder extends UiBinder<Widget, MainUI> {}
    private static Binder uiBinder = GWT.create(Binder.class);

    @UiField
    public MainMenu menu;

    @UiField
    public Toolbar toolbar;

    @UiField
    public LeftButtonRow leftButtons;

    @UiField
    public HTMLPanel canvas_wrapper;

    @UiField
    public HTMLPanel attribution;

    @UiField
    public HTMLPanel layers;

    @UiField(provided=true)
    public Canvas canv;

    public CanvasView canvView;

    public MainUI() {
        canv = Canvas.createIfSupported();
        canv.getElement().setId("canvas");

        canv.setCoordinateSpaceHeight(500);
        canv.setCoordinateSpaceWidth(600);

        initWidget(uiBinder.createAndBindUi(this));

        canvas_wrapper.getElement().setId("canvas-wrapper");

        attribution.getElement().setId("attribution");

        layers.getElement().setId("layers");
        layers.addStyleName("olControlLayerSwitcher");

        canvView = new CanvasViewImpl(canv);
    }

    static class CanvasViewImpl implements CanvasView {

        protected CanvasGraphics2D g;
        protected Canvas canv;
        protected CanvasPresenter presenter;

        public CanvasViewImpl(Canvas canv) {
            this.canv = canv;
        }

        @Override
        public int getWidth() {
            return canv.getCoordinateSpaceWidth();
        }

        @Override
        public int getHeight() {
            return canv.getCoordinateSpaceHeight();
        }

        @Override
        public Rectangle getBounds() {
            return new Rectangle(0, 0, getWidth(), getHeight());
        }

        @Override
        public void repaint() {
            presenter.repaint();
        }

        @Override
        public void setPresenter(CanvasPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public Graphics2D getGraphics2D() {
            if (g == null) {
                g = new CanvasGraphics2D(canv);
            }
            return g;
        }
    }

    public void setMapModesController(MapFrame mapModesController) {
        leftButtons.mapModesController = mapModesController;
    }
}
