package dark.utils;

import arc.files.Fi;
import arc.func.Intf;
import arc.graphics.*;
import arc.struct.Seq;
import arc.util.Tmp;
import dark.editor.Layer;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static dark.Main.editor;

public class Files {

    public static final Seq<String> extensions = Seq.with("spx", "png", "jpg", "jpeg", "bmp");

    // region read & write

    public static void read(Fi file) throws IOException {
        switch (file.extension()) {
            case "spx" -> {
                var layers = SPXFormat.read(file.read());
            }
            case "png", "jpg", "jpeg", "bmp" -> editor.reset(new Layer(file));
        }
    }

    public static void write(Fi file) throws IOException {
        switch (file.extension()) {
            case "spx" -> SPXFormat.write(file.write());
            case "png" -> {}
            case "jpg", "jpeg" -> {}
            case "bmp" -> {}
        }
    }

    // endregion
    // region convert

    public static BufferedImage convert(Pixmap pixmap, int type, Intf<Color> color) {
        var image = new BufferedImage(pixmap.width, pixmap.height, type);
        pixmap.each((x, y) -> image.setRGB(x, y, color.get(Tmp.c1.set(pixmap.get(x, y)))));

        return image;
    }

    public static Pixmap convert(BufferedImage image) {
        var pixmap = new Pixmap(image.getWidth(), image.getHeight());
        pixmap.each((x, y) -> pixmap.set(x, y, Tmp.c1.argb8888(image.getRGB(x, y)).rgba()));

        return pixmap;
    }

    // endregion
}