package dark.editor;

import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureAtlas.AtlasRegion;
import arc.math.Mathf;

import static arc.Core.atlas;

public class Paint {

    public static void line(float x, float y, float x2, float y2, float drawSize) {
        float length = Mathf.len(x2 - x, y2 - y);
        float drawX = (x2 - x) / length * drawSize / 2f, drawY = (y2 - y) / length * drawSize / 2f;

        Fill.quad(circleRegion(),
                x - drawX - drawY, y - drawY + drawX,
                x - drawX + drawY, y - drawY - drawX,
                x2 + drawX + drawY, y2 + drawY - drawX,
                x2 + drawX - drawY, y2 + drawY + drawX
        );
    }

    public static AtlasRegion circleRegion() {
        return atlas.find("circle");
    }
}