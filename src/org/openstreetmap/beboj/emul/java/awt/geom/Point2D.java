// License: GPL. For details, see LICENSE file
package java.awt.geom;

abstract public class Point2D {

    abstract public double getX();
    abstract public double getY();

    public static class Double extends Point2D {

        private double x;
        private double y;

        public Double(double x, double y) {
            this.x = x;
            this.y = y;
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

    public double distanceSq(Point2D other) {
        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        return dx * dx + dy * dy;
    }

    public double distance(Point2D other) {
        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
