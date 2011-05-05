// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;

import java.awt.Graphics2D;

public class CanvasGraphics2D extends Graphics2D {

    private Canvas c;

    public CanvasGraphics2D(Canvas c) {
        this.c = c;
    }

    public Context2d getContext2d() {
        return c.getContext2d();
    }

}
