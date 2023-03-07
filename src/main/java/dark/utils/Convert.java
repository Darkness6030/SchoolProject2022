package dark.utils;

import arc.graphics.Pixmap;
import arc.graphics.PixmapIO.PngWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Convert {

    public static Pixmap imageToPixmap(BufferedImage image) {
        var stream = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "png", stream);
            return new Pixmap(stream.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    public static BufferedImage pixmapToImage(Pixmap pixmap) {
        var writer = new PngWriter();
        var stream = new ByteArrayOutputStream();

        try {
            writer.setFlipY(false);
            writer.write(stream, pixmap);
            return ImageIO.read(new ByteArrayInputStream(stream.toByteArray()));
        } catch (Exception e) {
            return null;
        }
    }
}