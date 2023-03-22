package dark.ui;

import arc.graphics.Color;
import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.scene.style.Drawable;
import arc.scene.style.NinePatchDrawable;
import arc.scene.style.TextureRegionDrawable;
import arc.util.serialization.JsonValue;

import static arc.Core.*;
import static dark.Main.*;

public class Drawables {

    public static JsonValue splits;
    public static Drawable
            white_ui, white_rounded, white_rounded_left, white_rounded_right, white_knob,
            main, darkmain, active,
            main_rounded, darkmain_rounded, active_rounded,
            main_rounded_left, darkmain_rounded_left, active_rounded_left,
            main_rounded_right, darkmain_rounded_right, active_rounded_right,
            main_knob, darkmain_knob, active_knob,
            alpha_chan, alpha_chan_dizzy,
            underline, underline_red, cursor, selection,
            corners, gray, color_blob, slider_knob, switch_bg, empty, error;

    public static void load() {
        splits = reader.parse(files.internal("sprites/splits.json"));

        white_ui = load("whiteui", false);
        white_rounded = load("whiteui-rounded");
        white_rounded_left = load("whiteui-rounded-left");
        white_rounded_right = load("whiteui-rounded-right");
        white_knob = load("whiteui-knob");

        var white = (TextureRegionDrawable) white_ui;
        main = white.tint(Palette.main);
        darkmain = white.tint(Palette.darkmain);
        active = white.tint(Palette.active);

        main_rounded = tint(white_rounded, Palette.main);
        darkmain_rounded = tint(white_rounded, Palette.darkmain);
        active_rounded = tint(white_rounded, Palette.active);

        main_rounded_left = tint(white_rounded_left, Palette.main);
        darkmain_rounded_left = tint(white_rounded_left, Palette.darkmain);
        active_rounded_left = tint(white_rounded_left, Palette.active);

        main_rounded_right = tint(white_rounded_right, Palette.main);
        darkmain_rounded_right = tint(white_rounded_right, Palette.darkmain);
        active_rounded_right = tint(white_rounded_right, Palette.active);

        main_knob = tint(white_knob, Palette.main);
        darkmain_knob = tint(white_knob, Palette.darkmain);
        active_knob = tint(white_knob, Palette.active);

        alpha_chan = load("alpha-chan");
        alpha_chan_dizzy = load("alpha-chan-dizzy");

        underline = load("underline");
        underline_red = load("underline-red");
        cursor = load("cursor");
        selection = load("selection");

        corners = load("corners");
        gray = white.tint(0f, 0f, 0f, .5f);
        color_blob = load("color-blob");
        slider_knob = load("slider-knob");
        switch_bg = load("switch-bg");
        empty = load("empty");
        error = load("error");

        atlas.setErrorRegion("error");
    }

    public static Drawable load(String name) {
        return load(name, true);
    }

    public static Drawable load(String name, boolean linear) {
        loadRegion(name, linear);
        return atlas.drawable(name);
    }

    public static void loadRegion(String name, boolean linear) {
        Texture texture = new Texture("sprites/" + name + ".png");
        if (linear) texture.setFilter(TextureFilter.linear); // for better experience

        var region = atlas.addRegion(name, texture, 0, 0, texture.width, texture.height);
        if (splits.has(name)) region.splits = splits.get(name).asIntArray();
    }

    public static Drawable tint(Drawable from, Color tint) {
        return ((NinePatchDrawable) from).tint(tint);
    }
}