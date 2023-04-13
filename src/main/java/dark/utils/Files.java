package dark.utils;

import arc.files.Fi;
import arc.graphics.Pixmap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Files {

    public static void write(Pixmap pixmap, Fi file) throws IOException {
        ImageIO.write(convert(pixmap), file.extension(), file.file());
    }

    public static BufferedImage convert(Pixmap pixmap) throws IOException {
        var file = Fi.tempFile("convert");
        file.writePng(pixmap);

        return ImageIO.read(file.file());
    }

    public static Pixmap convert(BufferedImage image) throws IOException {
        var file = Fi.tempFile("convert");
        ImageIO.write(image, "png", file.file());

        return new Pixmap(file);
    }
}