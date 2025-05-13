package fractal.transformations;

import fractal.model.Point;

public class Horseshoe implements Transformation {
    @Override
    public Point apply(Point p) {
        double r = Math.sqrt(p.x() * p.x() + p.y() * p.y());
        double theta = Math.atan2(p.y(), p.x());
        return new Point(r * Math.cos(theta + Math.sin(theta)), r * Math.sin(theta + Math.sin(theta)));
    }
}
