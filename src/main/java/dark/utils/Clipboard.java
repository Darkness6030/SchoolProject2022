package dark.utils;

import arc.graphics.Pixmap;
import arc.util.Tmp;
import com.github.bsideup.jabel.Desugar;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Clipboard {

    public static Pixmap getImage() throws IOException, UnsupportedFlavorException {
        var contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (contents == null || !contents.isDataFlavorSupported(DataFlavor.imageFlavor)) return null;

        var image = (BufferedImage) contents.getTransferData(DataFlavor.imageFlavor);
        var pixmap = new Pixmap(image.getWidth(), image.getHeight());

        pixmap.each((x, y) -> pixmap.set(x, y, Tmp.c1.argb8888(image.getRGB(x, y)).rgba8888()));
        return pixmap;
    }

    public static void setImage(Pixmap pixmap) {
        var image = new BufferedImage(pixmap.width, pixmap.height, BufferedImage.TYPE_INT_RGB);
        pixmap.each((x, y) -> image.setRGB(x, y, Tmp.c1.rgba8888(pixmap.get(x, y)).argb8888()));

        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new ImageTransferable(image), null);
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