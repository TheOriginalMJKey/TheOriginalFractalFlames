package fractal.transformations;

import fractal.model.Point;

@SuppressWarnings("MultipleVariableDeclarations")
public class Waves implements Transformation {
    private final double b, c, e, f;

    public Waves(double b, double c, double e, double f) {
        this.b = b;
        this.c = c;
        this.e = e;
        this.f = f;
    }

    @Override
    public Point apply(Point p) {
        double x = p.x();
        double y = p.y();
        return new Point(x + b * Math.sin(y / (c * c)), y + e * Math.sin(x / (f * f)));
    }
}

