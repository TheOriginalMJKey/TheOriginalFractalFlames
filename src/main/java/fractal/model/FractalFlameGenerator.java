package fractal.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fractal.transformations.Transformation;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.experimental.UtilityClass;

@SuppressWarnings({"MagicNumber", "ParameterNumber", "RegexpSinglelineJava"})
@SuppressFBWarnings({"CLI_CONSTANT_LIST_INDEX", "UP_UNUSED_PARAMETER", "IMC_IMMATURE_CLASS_PRINTSTACKTRACE"})
@UtilityClass
public class FractalFlameGenerator {

    public static FractalImage render(
        FractalImage canvas,
        Rect world,
        List<Transformation> variations,
        int samples,
        int iterPerSample,
        long seed,
        int symmetry,
        Random random
    ) {
        return renderInternal(canvas, world, variations, samples, iterPerSample, seed, symmetry, random, 1);
    }

    public static FractalImage renderMultiThreaded(
        FractalImage canvas,
        Rect world,
        List<Transformation> variations,
        int samples,
        int iterPerSample,
        long seed,
        int symmetry,
        int numThreads,
        Random random
    ) {
        return renderInternal(canvas, world, variations, samples, iterPerSample, seed, symmetry, random, numThreads);
    }

    private static FractalImage renderInternal(
        FractalImage canvas,
        Rect world,
        List<Transformation> variations,
        int samples,
        int iterPerSample,
        long seed,
        int symmetry,
        Random random,
        int numThreads
    ) {
        initializeCanvas(canvas);
        int[][] colors = generateColors(variations, random);

        ExecutorService executor = numThreads > 1 ? Executors.newFixedThreadPool(numThreads) : null;

        for (int num = 0; num < samples; ++num) {
            final int sampleNum = num;
            Runnable task = () -> processSample(canvas, world, variations, iterPerSample, symmetry, colors, random);

            if (executor != null) {
                executor.execute(task);
            } else {
                task.run();
            }
        }

        if (executor != null) {
            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        normalizeColors(canvas);
        return canvas;
    }

    private static void initializeCanvas(FractalImage canvas) {
        for (Pixel pixel : canvas.data()) {
            pixel.setR(0);
            pixel.setG(0);
            pixel.setB(0);
            pixel.setHitCount(0);
        }
    }

    private static int[][] generateColors(List<Transformation> variations, Random random) {
        int[][] colors = new int[variations.size()][3];
        for (int i = 0; i < variations.size(); i++) {
            colors[i][0] = random.nextInt(256);
            colors[i][1] = random.nextInt(256);
            colors[i][2] = random.nextInt(256);
        }
        return colors;
    }

    private static void processSample(
        FractalImage canvas,
        Rect world,
        List<Transformation> variations,
        int iterPerSample,
        int symmetry,
        int[][] colors,
        Random random
    ) {
        Point pw = new Point(
            random.nextDouble() * world.width() + world.x(),
            random.nextDouble() * world.height() + world.y()
        );

        for (short step = -20; step < iterPerSample; ++step) {
            int variationIndex = random.nextInt(variations.size());
            Transformation variation = variations.get(variationIndex);
            pw = variation.apply(pw);

            double theta2 = 0.0;
            for (int s = 0; s < symmetry; theta2 += Math.PI * 2 / symmetry, ++s) {
                var pwr = rotate(pw, theta2);
                if (!world.contains(pwr)) {
                    continue;
                }

                Pixel pixel = mapRange(world, pwr, canvas);
                if (pixel == null) {
                    continue;
                }

                synchronized (pixel) {
                    updatePixel(pixel, colors[variationIndex]);
                }
            }
        }
    }

    private static void updatePixel(Pixel pixel, int[] color) {
        if (pixel.getHitCount() == 0) {
            pixel.setR(color[0]);
            pixel.setG(color[1]);
            pixel.setB(color[2]);
        } else {
            pixel.setR((pixel.getR() + color[0]) / 2);
            pixel.setG((pixel.getG() + color[1]) / 2);
            pixel.setB((pixel.getB() + color[2]) / 2);
        }
        pixel.setHitCount(pixel.getHitCount() + 1);
    }

    private static Point rotate(Point p, double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        return new Point(
            p.x() * cosTheta - p.y() * sinTheta,
            p.x() * sinTheta + p.y() * cosTheta
        );
    }

    private static Pixel mapRange(Rect world, Point p, FractalImage canvas) {
        int x = (int) ((p.x() - world.x()) / world.width() * canvas.getWidth());
        int y = (int) ((p.y() - world.y()) / world.height() * canvas.getHeight());
        if (canvas.contains(x, y)) {
            return canvas.pixel(x, y);
        }
        return null;
    }

    private static void normalizeColors(FractalImage canvas) {
        int maxHitCount = 0;

        for (Pixel pixel : canvas.data()) {
            if (pixel.getHitCount() > maxHitCount) {
                maxHitCount = pixel.getHitCount();
            }
        }

        if (maxHitCount == 0) {
            maxHitCount = 1;
        }

        for (Pixel pixel : canvas.data()) {
            if (pixel.getHitCount() == 0) {
                pixel.setR(0);
                pixel.setG(0);
                pixel.setB(0);
            } else {
                double ratio = Math.log(1 + pixel.getHitCount()) / Math.log(1 + maxHitCount);
                double gamma = 2.2;

                pixel.setR((int) (255 * Math.pow(pixel.getR() / 255.0, 1 / gamma) * ratio));
                pixel.setG((int) (255 * Math.pow(pixel.getG() / 255.0, 1 / gamma) * ratio));
                pixel.setB((int) (255 * Math.pow(pixel.getB() / 255.0, 1 / gamma) * ratio));
            }
        }
    }
}
