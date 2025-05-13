package fractal.transformations;

import fractal.model.Point;

public class Swirl implements Transformation {
    @Override
    public Point apply(Point p) {
        double r = Math.sqrt(p.x() * p.x() + p.y() * p.y());
        double theta = Math.atan2(p.y(), p.x()) + r;
        return new Point(r * Math.cos(theta), r * Math.sin(theta));
    }
}
