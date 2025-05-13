package fractal.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import lombok.experimental.UtilityClass;

@SuppressWarnings({"MagicNumber", "ParameterNumber", "RegexpSinglelineJava"})
@SuppressFBWarnings("IMC_IMMATURE_CLASS_PRINTSTACKTRACE")
@UtilityClass
public final class ImageUtils {
    public static void save(FractalImage image, Path filename, ImageFormat format) {
        BufferedImage bufferedImage =
            new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Pixel pixel = image.pixel(x, y);
                int rgb = (pixel.getR() << 16) | (pixel.getG() << 8) | pixel.getB();
                bufferedImage.setRGB(x, y, rgb);
            }
        }

        try {
            ImageIO.write(bufferedImage, format.name().toLowerCase(), filename.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
