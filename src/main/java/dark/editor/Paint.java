package dark.editor;

import arc.math.geom.Bresenham2;
import arc.math.geom.Point2;
import arc.struct.IntQueue;

import static dark.editor.Editor.none;

public class Paint {

    public static void draw(Layer layer, int x1, int y1, int x2, int y2, int drawSize, int color) {
        if (x1 != none && y1 != none) line(layer, x1, y1, x2, y2, drawSize, color);
        else circle(layer, x2, y2, drawSize, color);
    }

    public static void line(Layer layer, int x1, int y1, int x2, int y2, int drawSize, int color) {
        Bresenham2.line(x1, y1, x2, y2, (x, y) -> circle(layer, x, y, drawSize, color));
    }

    public static void circle(Layer layer, int x, int y, int drawSize, int color) {
        layer.fillCircle(x, y, drawSize, color);
    }

    public static void fill(Layer layer, int x, int y, int color) {
        int prevColor = layer.get(x, y);
        var hits = new boolean[layer.width][layer.height];

        var queue = new IntQueue();
        queue.addLast(Point2.pack(x, y));

        while (!queue.isEmpty()) {
            int pos = queue.removeFirst();
            x = Point2.x(pos);
            y = Point2.y(pos);

            if (layer.in(x, y) && !hits[x][y] && layer.get(x, y) == prevColor) {
                layer.set(x, y, color);
                hits[x][y] = true;

                queue.addLast(Point2.pack(x, y - 1));
                queue.addLast(Point2.pack(x, y + 1));
                queue.addLast(Point2.pack(x - 1, y));
                queue.addLast(Point2.pack(x + 1, y));
            }
        }
    }
}