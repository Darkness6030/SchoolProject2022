package dark.history;

import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.struct.IntSeq;
import dark.editor.Layer;

public class Compress {

    public static IntSeq write(Pixmap pixmap) {
        IntSeq result = new IntSeq();

        for (int x = 0; x < pixmap.width; x++) {
            for (int y = 0; y < pixmap.height; y++) {
                int raw = pixmap.getRaw(x, y);
                if (raw == Color.clearRgba) continue; // skip empty pixels

                int length = 1;
                for (int i = y + 1; i < pixmap.height; i++)
                    if (pixmap.getRaw(i, y) == raw) length++;
                    else break;

                result.addAll(x, y, length, raw);
                y += length - 1; // skip same pixels
            }
        }

        return result;
    }

    public static IntSeq difference(Pixmap before, Layer after) {
        before.each((x, y) -> {
            if (before.getRaw(x, y) == after.getRaw(x, y)) before.set(x, y, Color.clearRgba);
        });
        return write(before);
    }
}
