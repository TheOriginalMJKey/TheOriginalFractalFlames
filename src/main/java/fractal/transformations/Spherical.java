package fractal.transformations;

import fractal.model.Point;

public class Spherical implements Transformation {
    @Override
    public Point apply(Point p) {
        double r = Math.sqrt(p.x() * p.x() + p.y() * p.y());
        if (r == 0) {
            return p;
        }
        return new Point(p.x() / (r * r), p.y() / (r * r));
    }
}
