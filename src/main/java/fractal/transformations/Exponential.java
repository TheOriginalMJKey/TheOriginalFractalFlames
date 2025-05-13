package fractal.transformations;

import fractal.model.Point;

public class Exponential implements Transformation {
    @Override
    public Point apply(Point p) {
        double x = p.x();
        double y = p.y();
        return new Point(Math.exp(x - 1) * Math.cos(Math.PI * y), Math.exp(x - 1) * Math.sin(Math.PI * y));
    }
}
