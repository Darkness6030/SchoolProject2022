package dark.utils;

import arc.graphics.Pixmap;
import arc.util.Log;

import javax.imageio.ImageIO;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;

public class ClipboardUtils {

    public static Pixmap getImage() {
        try {
            var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            return ImageUtils.imageToPixmap((BufferedImage) clipboard.getData(DataFlavor.imageFlavor));
        } catch (Exception e) {
            Log.err(e);
            return null;
        }
    }

    public static void setImage(Pixmap pixmap) {
        try {
            var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            var image = ImageUtils.pixmapToImage(pixmap);
            clipboard.setContents(new Transferable() {
                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[] { DataFlavor.imageFlavor };
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return flavor == DataFlavor.imageFlavor;
                }

                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                    if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);

                    return image;
                }
            }, null);
        } catch (Exception e) {
            Log.err(e);
        }
    }
}