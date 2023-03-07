package dark.utils;

import arc.graphics.Pixmap;
import com.github.fracpete.jclipboardhelper.ClipboardHelper;

public class Clipboard {

    public static Pixmap getImage() {
        return Convert.imageToPixmap(ClipboardHelper.pasteImageFromClipboard());
    }

    public static void setImage(Pixmap pixmap) {
        ClipboardHelper.copyToClipboard(Convert.pixmapToImage(pixmap));
    }
}