package fractal.transformations;

import fractal.model.Point;

public class Polar implements Transformation {
    @Override
    public Point apply(Point p) {
        double r = Math.sqrt(p.x() * p.x() + p.y() * p.y());
        double theta = Math.atan2(p.y(), p.x());
        return new Point(theta / Math.PI, r - 1);
    }
}
