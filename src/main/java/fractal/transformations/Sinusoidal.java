package fractal.transformations;

import fractal.model.Point;

public class Sinusoidal implements Transformation {
    @Override
    public Point apply(Point p) {
        return new Point(Math.sin(p.x()), Math.sin(p.y()));
    }
}
