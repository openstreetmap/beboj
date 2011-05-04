// License: GPL. For details, see LICENSE file
package java.awt;

public class Rectangle {

    public int x, y, width, height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean contains(Point p) {
        return p.x >= x && p.y >= y && p.x <= x + width && p.y <= y + height;
    }

}
