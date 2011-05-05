// License: GPL. See LICENSE file for details.
package org.openstreetmap.josm.beboj;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Rectangular component, with independent resize management.
 * However, painting is managed by the CanvasPresenter.
 */
public interface CanvasView {

    void setPresenter(CanvasPresenter p);
    
    int getWidth();

    int getHeight();

    Rectangle getBounds();

    void repaint();

    Graphics2D getGraphics2D();
}
