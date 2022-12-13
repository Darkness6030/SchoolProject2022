package dark.ui;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.struct.ObjectMap;
import arc.util.Tmp;

public class Icons {

    public static final ObjectMap<String, Drawable> drawables = new ObjectMap<>();

    public static Drawable pencil, eraser, pick, fill,
            ok, back, exit, home, swap,
            save, load, folder, file,
            up, down, right, left,
            plus;

    public static void load() {
        pencil = load("pencil", '\uE869');
        eraser = load("eraser", '\uF12D');
        pick = load("pick", '\uE877');
        fill = load("fill", '\uE84C');

        ok = load('\uE800');
        back = load('\uE802');
        exit = load('\uE85F');
        home = load('\uE807');
        swap = load('\uE823');

        save = load('\uE81B');
        load = load('\uE87B');
        folder = load('\uE81D');
        file = load('\uF15C');

        up = load('\uE804');
        down = load('\uE805');
        right = load('\uE803');
        left = load('\uE802');

        plus = load('\uE813');
    }

    public static Drawable getDrawable(String name) {
        return drawables.get(name, Drawables.error);
    }

    public static Drawable load(String name, char symbol) {
        var drawable = load(symbol);
        drawables.put(name, drawable);
        return drawable;
    }

    public static Drawable load(char symbol) {
        return load(Fonts.def, symbol);
    }

    public static Drawable load(Font font, char symbol) {
        var glyph = font.getData().getGlyph(symbol);
        var drawable = new TextureRegionDrawable(new TextureRegion(font.getRegion().texture, glyph.u, glyph.v2, glyph.u2, glyph.v)) {
            @Override
            public void draw(float x, float y, float width, float height) {
                Draw.color(Tmp.c1.set(tint).mul(Draw.getColor()));
                Draw.rect(region, x + width / 2f, y + height / 2f, glyph.width, glyph.height);
            }

            @Override
            public void draw(float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
                width *= scaleX;
                height *= scaleY;

                Draw.color(Tmp.c1.set(tint).mul(Draw.getColor()));
                Draw.rect(region, x + width / 2f, y + height / 2f, glyph.width, glyph.height, glyph.width / 2f, glyph.height / 2f, rotation);
            }

            @Override
            public float imageSize() {
                return Math.max(glyph.width, glyph.height);
            }
        };

        drawable.setMinWidth(drawable.imageSize());
        drawable.setMinHeight(drawable.imageSize());

        return drawable;
    }
}