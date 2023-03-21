package dark.utils;

import arc.func.Intc2;
import arc.math.Mathf;
import arc.math.geom.Geometry;

public class Shapes {

    public static void square(int x, int y, int size, Intc2 cons) {
        for (int cx = 0; cx < size; cx++)
            for (int cy = 0; cy < size; cy++)
                cons.get(x + cx - size / 2, y + cy - size / 2);
    }

    public static void circle(int x, int y, int size, Intc2 cons) {
        Geometry.circle(x, y, Mathf.ceil(size / 2f), cons);
    }
}