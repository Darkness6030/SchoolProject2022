package dark.ui;

import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.graphics.g2d.TextureAtlas.AtlasRegion;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.util.serialization.Jval;

import static arc.Core.*;

public class Textures {

    public static Jval splits;
    public static Drawable whiteui, black, color_blob, underline, sideline, slider_back, slider_knob, slider_knob_over, slider_knob_down;

    public static void load() {
        splits = Jval.read(files.internal("sprites/splits.json").reader());

        whiteui = load("whiteui");
        black = ((TextureRegionDrawable) whiteui).tint(0f, 0f, 0f, .5f);
        color_blob = load("color-blob");
        underline = load("underline");
        sideline = load("sideline");
        slider_back = load("slider-back");
        slider_knob = load("slider-knob");
        slider_knob_over = load("slider-knob-over");
        slider_knob_down = load("slider-knob-down");
    }

    public static Drawable load(String name) {
        Texture texture = new Texture("sprites/" + name + ".png");
        texture.setFilter(TextureFilter.linear); // for better experience

        // add texture to atlas and get region to modify splits
        AtlasRegion region = atlas.addRegion(name, texture, 0, 0, texture.width, texture.height);

        if (splits.has(name)) region.splits = splits.get(name).asArray().mapInt(Jval::asInt).items;
        return atlas.drawable(name);
    }
}