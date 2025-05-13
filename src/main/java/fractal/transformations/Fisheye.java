package fractal.transformations;

import fractal.model.Point;

public class Fisheye implements Transformation {
    @Override
    public Point apply(Point p) {
        double r = Math.sqrt(p.x() * p.x() + p.y() * p.y());
        return new Point((2 / (r + 1)) * p.y(), (2 / (r + 1)) * p.x());
    }
}


