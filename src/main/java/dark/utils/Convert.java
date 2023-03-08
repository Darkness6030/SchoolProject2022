package dark.utils;

import arc.graphics.Pixmap;
import arc.graphics.PixmapIO.PngWriter;
import arc.util.Log;

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
            Log.err(e);
            return null;
        }
    }

    public static BufferedImage pixmapToImage(Pixmap pixmap) {
        var writer = new PngWriter();
        var stream = new ByteArrayOutputStream();

        try {
            writer.setFlipY(false);
            writer.write(stream, pixmap);

            var image = new BufferedImage(pixmap.width, pixmap.height, BufferedImage.TYPE_INT_RGB);
            image.getGraphics().drawImage(ImageIO.read(new ByteArrayInputStream(stream.toByteArray())), 0, 0, null);
            return image;
        } catch (Exception e) {
            Log.err(e);
            return null;
        }
    }
}