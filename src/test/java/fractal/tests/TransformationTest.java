package fractal.tests;

import fractal.model.Point;
import fractal.transformations.Heart;
import fractal.transformations.Polar;
import fractal.transformations.Sinusoidal;
import fractal.transformations.Spherical;
import fractal.transformations.Spiral;
import fractal.transformations.Transformation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

 class TransformationTest {

    @Test
     void testSpiralTransformation() {
        Transformation spiral = new Spiral();
        Point p = new Point(1.0, 1.0);
        Point expected = new Point((Math.cos(Math.atan2(1.0, 1.0)) + Math.sin(Math.sqrt(2.0))) / Math.sqrt(2.0),
            (Math.sin(Math.atan2(1.0, 1.0)) - Math.cos(Math.sqrt(2.0))) / Math.sqrt(2.0));
        Point result = spiral.apply(p);
        assertEquals(expected.x(), result.x(), 1e-6);
        assertEquals(expected.y(), result.y(), 1e-6);
    }

    @Test
     void testSinusoidalTransformation() {
        Transformation sinusoidal = new Sinusoidal();
        Point p = new Point(1.0, 1.0);
        Point expected = new Point(Math.sin(1.0), Math.sin(1.0));
        Point result = sinusoidal.apply(p);
        assertEquals(expected.x(), result.x(), 1e-6);
        assertEquals(expected.y(), result.y(), 1e-6);
    }

    @Test
     void testSphericalTransformation() {
        Transformation spherical = new Spherical();
        Point p = new Point(1.0, 1.0);
        Point result = spherical.apply(p);
        assertEquals(0.5, result.x(), 1e-10);
        assertEquals(0.5, result.y(), 1e-10);
    }

    @Test
     void testPolarTransformation() {
        Transformation polar = new Polar();
        Point p = new Point(1.0, 1.0);
        Point expected = new Point(Math.atan2(1.0, 1.0) / Math.PI, Math.sqrt(2.0) - 1);
        Point result = polar.apply(p);
        assertEquals(expected.x(), result.x(), 1e-6);
        assertEquals(expected.y(), result.y(), 1e-6);
    }

    @Test
     void testHeartTransformation() {
        Transformation heart = new Heart();
        Point p = new Point(1.0, 1.0);
        Point expected = new Point(Math.sqrt(2.0) * Math.sin(Math.atan2(1.0, 1.0) * Math.sqrt(2.0)),
            -Math.sqrt(2.0) * Math.cos(Math.atan2(1.0, 1.0) * Math.sqrt(2.0)));
        Point result = heart.apply(p);
        assertEquals(expected.x(), result.x(), 1e-6);
        assertEquals(expected.y(), result.y(), 1e-6);
    }
}


