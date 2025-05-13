package fractal.transformations;

import fractal.model.Point;

public class Ex implements Transformation {
    @Override
    public Point apply(Point p) {
        double r = Math.sqrt(p.x() * p.x() + p.y() * p.y());
        double theta = Math.atan2(p.y(), p.x());
        double p1 = Math.sin(theta + r);
        double p2 = Math.cos(theta - r);
        return new Point(r * (p1 * p1 * p1 + p2 * p2 * p2), r * (p1 * p1 * p1 - p2 * p2 * p2));
    }
}



