package dark.editor;

import arc.graphics.Color;
import arc.math.geom.Bresenham2;

import static dark.editor.Editor.none;

public class Paint {

    public static void draw(Layer layer, int x1, int y1, int x2, int y2, int drawSize, Color color) {
        if (x1 != none && y1 != none) line(layer, x1, y1, x2, y2, drawSize, color);
        else circle(layer, x2, y2, drawSize, color);
    }

    public static void line(Layer layer, int x1, int y1, int x2, int y2, int drawSize, Color color) {
        Bresenham2.line(x1, y1, x2, y2, (x, y) -> circle(layer, x, y, drawSize, color));
    }

    public static void circle(Layer layer, int x, int y, int drawSize, Color color) {
        layer.fillCircle(x, y, drawSize, color.rgba());
    }
}