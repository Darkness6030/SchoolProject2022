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
        return Files.convert(image);
    }

    public static void copy(Pixmap pixmap) throws IOException {
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new ImageTransferable(Files.convert(pixmap)), null);
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