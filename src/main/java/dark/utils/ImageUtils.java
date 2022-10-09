package dark.utils;

import arc.graphics.Pixmap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;

public class ImageUtils {

    public static BufferedImage pixmapToImage(Pixmap pixmap) {
        try {
            return ImageIO.read(new ByteArrayInputStream(pixmap.getPixels().array()));
        } catch (Exception e) {
            return null;
        }
    }

    public static Pixmap imageToPixmap(BufferedImage image) {
        try {
            return new Pixmap(((DataBufferByte) image.getRaster().getDataBuffer()).getData());
        } catch (Exception e) {
            return null;
        }
    }
}