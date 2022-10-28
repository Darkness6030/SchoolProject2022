package dark.editor;

import arc.graphics.Color;
import arc.math.geom.Bresenham2;

public class Paint {

    public static void line(Layer layer, int x1, int y1, int x2, int y2, int drawSize, Color color) {
        Bresenham2.line(x1, y1, x2, y2, (x, y) -> layer.fillCircle(x, y, drawSize, color.rgba()));
    }

    public static void circle(Layer layer, int x, int y, int drawSize, Color color) {
        layer.fillCircle(x, y, drawSize, color.rgba());
    }
}