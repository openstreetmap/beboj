// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.openstreetmap.josm.beboj.CanvasPresenter;
import org.openstreetmap.josm.beboj.CanvasView;

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

    public CanvasView canvView;

    public MainUI() {
        canv = Canvas.createIfSupported();
        canv.getElement().setId("canvas");

        canv.setCoordinateSpaceHeight(500);
        canv.setCoordinateSpaceWidth(600);

        initWidget(uiBinder.createAndBindUi(this));

        canvView = new CanvasViewImpl(canv);
    }

    static class CanvasViewImpl implements CanvasView {
        
        CanvasGraphics2D g;
        Canvas canv;
        CanvasPresenter p;

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
            p.repaint();
        }
        
        @Override
        public void setPresenter(CanvasPresenter p) {
            this.p = p;
        }
        
        @Override
        public Graphics2D getGraphics2D() {
            if (g == null) {
                g = new CanvasGraphics2D(canv);
            }
            return g;
        }
    }
}
