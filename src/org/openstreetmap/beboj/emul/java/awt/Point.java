// License: GPL. For details, see LICENSE file
package java.awt;

import java.awt.geom.Point2D;

public class Point extends Point2D {

    public int x;
    public int y;


    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

}
