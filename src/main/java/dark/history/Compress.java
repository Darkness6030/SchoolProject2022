package dark.history;

import arc.graphics.*;
import arc.struct.IntSeq;

public class Compress {

    public static IntSeq write(Pixmap pixmap) {
        var result = new IntSeq();

        for (int x = 0; x < pixmap.width; x++) {
            for (int y = 0; y < pixmap.height; y++) {
                int raw = pixmap.getRaw(x, y);
                if (raw == Color.clearRgba) continue; // skip empty pixels

                int length = 1;
                for (int i = y + 1; i < pixmap.height; i++)
                    if (pixmap.getRaw(x, i) == raw) length++;
                    else break;

                result.addAll(x, y, length, raw);
                y += length - 1; // skip same pixels
            }
        }

        return result;
    }

    public static void read(IntSeq data, ColorConsumer cons) {
        for (int i = 0; i < data.size; i++) {
            int x = data.get(i++), y = data.get(i++), length = data.get(i++);

            int raw = data.get(i);
            for (int j = 0; j < length; j++)
                cons.get(x, y + j, raw);
        }
    }

    public static IntSeq difference(Pixmap before, Pixmap after) {
        before.each((x, y) -> before.set(x, y, after.getRaw(x, y) - before.getRaw(x, y)));
        return write(before);
    }

    public interface ColorConsumer {
        void get(int x, int y, int color);
    }
}