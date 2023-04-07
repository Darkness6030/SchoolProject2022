package dark.utils;

import arc.files.Fi;
import arc.graphics.*;
import com.github.bsideup.jabel.Desugar;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Clipboard {

    public static Pixmap paste() throws IOException, UnsupportedFlavorException {
        var contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (contents == null || !contents.isDataFlavorSupported(DataFlavor.imageFlavor)) return null;

        var image = (BufferedImage) contents.getTransferData(DataFlavor.imageFlavor);
        return convert(image);
    }

    public static void copy(Pixmap pixmap) throws IOException {
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new ImageTransferable(convert(pixmap)), null);
    }

    public static BufferedImage convert(Pixmap pixmap) throws IOException {
        var file = Fi.tempFile("temp");
        PixmapIO.writePng(file, pixmap);

        return ImageIO.read(file.file());
    }

    public static Pixmap convert(BufferedImage image) throws IOException {
        var file = Fi.tempFile("temp");
        ImageIO.write(image, "png", file.file());

        return PixmapIO.readPNG(file);
    }

    @Desugar
    public record ImageTransferable(BufferedImage image) implements Transferable {
        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] {DataFlavor.imageFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor == DataFlavor.imageFlavor;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) {
            return image;
        }
    }
}