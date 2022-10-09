package dark.ui;

import arc.graphics.Texture;
import arc.graphics.g2d.TextureAtlas.AtlasRegion;
import arc.scene.style.Drawable;
import arc.util.serialization.Jval;

import static arc.Core.*;

public class Textures {

    public static Jval splits;
    public static Drawable alpha_bg, alpha_bg_line, underline, slider_back, slider_knob, slider_knob_over, slider_knob_down;

    public static void load() {
        splits = Jval.read(files.internal("sprites/splits.json").reader());

        alpha_bg = load("alpha-bg");
        alpha_bg_line = load("alpha-bg-line");
        underline = load("underline");
        slider_back = load("slider-back");
        slider_knob = load("slider-knob");
        slider_knob_over = load("slider-knob-over");
        slider_knob_down = load("slider-knob-down");
    }

    public static Drawable load(String name) {
        Texture texture = new Texture("sprites/" + name + ".png");
        AtlasRegion region = atlas.addRegion(name, texture, 0, 0, texture.width, texture.height);

        if (splits.has(name)) region.splits = splits.get(name).asArray().mapInt(Jval::asInt).items;
        return atlas.drawable(name);
    }
}