package dark.editor;

import arc.graphics.Color;
import arc.math.geom.Point2;
import arc.struct.IntQueue;

import static dark.Main.editor;

public class Paint {

    public static void pencil(Layer layer, int x, int y, int brushSize, Color color) {
        if (editor.square)
            layer.drawSquare(x, y, brushSize, color);
        else
            layer.drawCircle(x, y, brushSize, color);
    }

    public static void fill(Layer layer, int x, int y, Color color) {
        //int prevColor = layer.get(x, y);
        //var hits = new boolean[layer.width][layer.height];

        var queue = new IntQueue();
        queue.addLast(Point2.pack(x, y));

        while (!queue.isEmpty()) {
            int pos = queue.removeFirst();
            x = Point2.x(pos);
            y = Point2.y(pos);

            //if (layer.in(x, y) && !hits[x][y] && layer.get(x, y) == prevColor) {
            //    layer.set(x, y, color);
            //    hits[x][y] = true;

                queue.addLast(Point2.pack(x, y - 1));
                queue.addLast(Point2.pack(x, y + 1));
                queue.addLast(Point2.pack(x - 1, y));
                queue.addLast(Point2.pack(x + 1, y));
            //}
        }
    }
}