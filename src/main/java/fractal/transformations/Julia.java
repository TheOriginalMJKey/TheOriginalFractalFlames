package fractal.transformations;

import fractal.model.Point;

public class Julia implements Transformation {
    private final double omega;

    public Julia(double omega) {
        this.omega = omega;
    }

    @Override
    public Point apply(Point p) {
        double r = Math.sqrt(p.x() * p.x() + p.y() * p.y());
        double theta = Math.atan2(p.y(), p.x());
        double omegat = omega * theta;
        return new Point(r * Math.cos(omegat), r * Math.sin(omegat));
    }
}

