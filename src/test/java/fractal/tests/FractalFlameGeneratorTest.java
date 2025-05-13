package fractal.tests;


import fractal.model.FractalFlameGenerator;
import fractal.model.FractalImage;
import fractal.model.Rect;
import fractal.transformations.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;


 class FractalFlameGeneratorTest {
    @Test
     void testPerformance() {
        FractalImage canvas = FractalImage.create(800, 600);
        Rect world = new Rect(-2.0, -2.0, 4.0, 4.0);
        List<Transformation> variations = new ArrayList<>();
        variations.add(new Spiral());
        variations.add(new Spherical());
        int samples = 10000;
        int iterPerSample = 100;
        long seed = 42;
        int symmetry = 1;
        int numThreads = 4;
        Random random = new Random(seed);

        long startTimeSingle = System.nanoTime();
        FractalImage resultSingle = FractalFlameGenerator.render(canvas, world, variations, samples, iterPerSample, seed, symmetry, random);
        long endTimeSingle = System.nanoTime();
        long durationSingle = (endTimeSingle - startTimeSingle) / 1_000_000;

        long startTimeMulti = System.nanoTime();
        FractalImage resultMulti = FractalFlameGenerator.renderMultiThreaded(canvas, world, variations, samples, iterPerSample, seed, symmetry, numThreads, random);
        long endTimeMulti = System.nanoTime();
        long durationMulti = (endTimeMulti - startTimeMulti) / 1_000_000;

        System.out.println("Single-threaded duration: " + durationSingle + " ms");
        System.out.println("Multi-threaded duration: " + durationMulti + " ms");
        assertTrue(durationMulti < durationSingle, "Multi-threaded version should be faster than single-threaded version");
    }
}


