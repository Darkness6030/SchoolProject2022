package dark.utils;

import arc.func.Intc2;
import arc.math.Mathf;
import arc.math.geom.*;

public class Shapes {

    public static void square(int x, int y, int size, Intc2 cons) {
        for (int cx = 0; cx < size; cx++)
            for (int cy = 0; cy < size; cy++)
                cons.get(x + cx - size / 2, y + cy - size / 2);
    }

    public static void circle(int x, int y, int size, Intc2 cons) {
        Geometry.circle(x, y, Mathf.ceil(size / 2f), cons);
    }

    public static void line(int x1, int y1, int x2, int y2, boolean orthogonal, Intc2 cons) {
        if (orthogonal) {
            if (Math.abs(x2 - x1) < Math.abs(y2 - y1))
                x2 = x1;
            else
                y2 = y1;
        }

        Bresenham2.line(x1, y1, x2, y2, cons);
    }
}