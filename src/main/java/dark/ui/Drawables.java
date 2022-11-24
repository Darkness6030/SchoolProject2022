package dark.ui;

import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.util.serialization.JsonValue;

import static arc.Core.*;
import static dark.Main.reader;

public class Drawables {

    public static JsonValue splits;
    public static Drawable circle, error,
            whiteui, black, color_blob,
            underline, sideline, sideline_left,
            slider_back, slider_knob, slider_knob_over, slider_knob_down,
            pencil, eraser, pick, fill;

    public static void load() {
        splits = reader.parse(files.internal("sprites/splits.json"));

        circle = load("circle", false);
        error = load("error");

        whiteui = load("whiteui");
        black = ((TextureRegionDrawable) whiteui).tint(0f, 0f, 0f, .5f);
        color_blob = load("color-blob");

        underline = load("underline");
        sideline = load("sideline");
        sideline_left = load("sideline-left");

        slider_back = load("slider-back");
        slider_knob = load("slider-knob");
        slider_knob_over = load("slider-knob-over");
        slider_knob_down = load("slider-knob-down");

        atlas.setErrorRegion("error");
    }

    public static Drawable load(String name) {
        return load(name, true);
    }

    public static Drawable load(String name, boolean linear) {
        var texture = new Texture("sprites/" + name + ".png");
        if (linear) texture.setFilter(TextureFilter.linear); // for better experience

        // add texture to atlas and get region to modify splits
        var region = atlas.addRegion(name, texture, 0, 0, texture.width, texture.height);

        if (splits.has(name)) region.splits = splits.get(name).asIntArray();
        return atlas.drawable(name);
    }
}