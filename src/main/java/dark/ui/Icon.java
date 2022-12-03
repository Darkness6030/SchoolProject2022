package dark.ui;

import arc.graphics.g2d.Font;
import arc.scene.style.Drawable;

public class Icon {

    public static Drawable pencil, eraser, pick, fill,
            ok, back, exit, swap,
            save, load, folder, file,
            up, down, right, left;

    public static void load() {
        pencil = load('\uE869');
        eraser = load('\uF12D');
        pick = load('\uE877');
        fill = load('\uE84C');

        ok = load('\uE800');
        back = load('\uE802');
        exit = load('\uE85F');
        swap = load('\uE823');

        save = load('\uE81B');
        load = load('\uE87B');
        folder = load('\uE81D');
        file = load('\uF15C');

        up = load('\uE804');
        down = load('\uE805');
        right = load('\uE803');
        left = load('\uE802');
    }

    public static Drawable load(char symbol) {
        return load(Fonts.def, symbol);
    }

    public static Drawable load(Font font, char symbol) {
        return mindustry.ui.Fonts.getGlyph(font, symbol);
    }
}
