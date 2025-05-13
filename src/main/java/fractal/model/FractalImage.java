package fractal.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

@SuppressWarnings("MagicNumber")
@SuppressFBWarnings("PATH_TRAVERSAL_IN")
public record FractalImage(Pixel[] data, int width, int height) {
    public static FractalImage create(int width, int height) {
        Pixel[] data = new Pixel[width * height];
        for (int i = 0; i < data.length; i++) {
            data[i] = new Pixel(0, 0, 0, 0);
        }
        return new FractalImage(data, width, height);
    }

    public boolean contains(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Pixel pixel(int x, int y) {
        return data[y * width + x];
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public static FractalImage load(String filePath) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(filePath));
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        Pixel[] data = new Pixel[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = bufferedImage.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                data[y * width + x] = new Pixel(r, g, b, 0);
            }
        }

        return new FractalImage(data, width, height);
    }
}
