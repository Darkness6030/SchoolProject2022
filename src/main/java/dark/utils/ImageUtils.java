package dark.utils;

import arc.graphics.Pixmap;
import arc.graphics.PixmapIO.PngWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageUtils {

    public static BufferedImage pixmapToImage(Pixmap pixmap) {
        try {
            var stream = new ByteArrayOutputStream();
            var writer = new PngWriter();

            writer.setFlipY(false);
            writer.write(stream, pixmap);

            return ImageIO.read(new ByteArrayInputStream(stream.toByteArray()));
        } catch (Exception e) {
            return null;
        }
    }

    public static Pixmap imageToPixmap(BufferedImage image) {
        try {
            var stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);

            return new Pixmap(stream.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }
}