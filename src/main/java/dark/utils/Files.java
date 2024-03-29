package dark.utils;

import arc.files.Fi;
import arc.graphics.Pixmap;
import arc.struct.Seq;
import arc.util.Tmp;
import dark.editor.Layer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static dark.Main.*;

public class Files {

    public static final Seq<String> extensions = Seq.with("spx", "png", "jpg", "jpeg", "bmp");

    // region read & write

    public static void read(Fi file) throws IOException {
        switch (file.extension()) {
            case "spx" -> editor.reset(SPXFormat.read(file.read()));
            case "png" -> editor.reset(new Layer(file));
            case "jpg", "jpeg", "bmp" -> {
                var image = ImageIO.read(file.file());

                var pixmap = convert(image, false);
                editor.reset(new Layer(pixmap));

                pixmap.dispose();
            }
        }
    }

    public static void write(Fi file) throws IOException {
        switch (file.extension()) {
            case "spx" -> SPXFormat.write(file.write());
            case "png" -> {
                var pixmap = editor.renderer.copy();
                file.writePng(pixmap);
                pixmap.dispose();
            }
            case "jpg", "jpeg", "bmp" -> {
                var pixmap = editor.renderer.copy();

                var image = convert(pixmap, false);
                ImageIO.write(image, file.extension(), file.file());

                pixmap.dispose();
            }
        }
    }

    // endregion
    // region convert

    public static BufferedImage convert(Pixmap pixmap, boolean transparent) {
        var image = new BufferedImage(pixmap.width, pixmap.height, transparent ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        pixmap.each((x, y) -> image.setRGB(x, y, transparent ? Tmp.c1.set(pixmap.get(x, y)).argb8888() : Tmp.c1.set(pixmap.get(x, y)).rgb888()));

        return image;
    }

    public static Pixmap convert(BufferedImage image, boolean transparent) {
        var pixmap = new Pixmap(image.getWidth(), image.getHeight());
        pixmap.each((x, y) -> pixmap.set(x, y, transparent ? Tmp.c1.argb8888(image.getRGB(x, y)).rgba() : Tmp.c1.rgb888(image.getRGB(x, y)).rgba()));

        return pixmap;
    }

    // endregion
}