package dark.utils;

import arc.graphics.Pixmap;
import com.github.bsideup.jabel.Desugar;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Clipboard {

    public static Pixmap getImage() throws IOException, UnsupportedFlavorException {
        var image = (BufferedImage) Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .getContents(null)
                .getTransferData(DataFlavor.imageFlavor);

        var pixmap = new Pixmap(image.getWidth(), image.getHeight());
        pixmap.each((x, y) -> pixmap.set(x, y, image.getRGB(x, y)));
        return pixmap;
    }

    public static void setImage(Pixmap pixmap) {
        var image = new BufferedImage(pixmap.width, pixmap.height, BufferedImage.TYPE_INT_RGB);
        pixmap.each((x, y) -> image.setRGB(x, y, pixmap.get(x, y)));

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