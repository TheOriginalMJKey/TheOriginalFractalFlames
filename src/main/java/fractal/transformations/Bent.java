package fractal.transformations;

import fractal.model.Point;

public class Bent implements Transformation {
    @Override
    public Point apply(Point p) {
        double x = p.x();
        double y = p.y();
        if (x >= 0 && y >= 0) {
            return new Point(x, y);
        } else if (x < 0 && y >= 0) {
            return new Point(2 * x, y);
        } else if (x >= 0 && y < 0) {
            return new Point(x, y / 2);
        } else {
            return new Point(2 * x, y / 2);
        }
    }
}

