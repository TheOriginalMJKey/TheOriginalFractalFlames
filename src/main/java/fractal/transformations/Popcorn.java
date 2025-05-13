package fractal.transformations;

import fractal.model.Point;

@SuppressWarnings({"MagicNumber", "MultipleVariableDeclarations"})
public class Popcorn implements Transformation {
    private final double c, f;

    public Popcorn(double c, double f) {
        this.c = c;
        this.f = f;
    }

    @Override
    public Point apply(Point p) {
        double x = p.x();
        double y = p.y();
        return new Point(x + c * Math.sin(Math.tan(3 * y)), y + f * Math.sin(Math.tan(3 * x)));
    }
}

