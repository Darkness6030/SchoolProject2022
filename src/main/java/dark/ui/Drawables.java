package dark.ui;

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
            white, white_rounded, white_rounded_left, white_rounded_right, white_knob,
            main, darkmain, active,
            main_rounded, darkmain_rounded, active_rounded,
            main_knob, darkmain_knob, active_knob,
            alpha_chan, alpha_chan_dizzy,
            underline, underline_red, cursor, selection,
            corners, gray, color_blob, switch_bg, empty, error;

    public static void load() {
        splits = reader.parse(files.internal("sprites/splits.json"));

        white = load("whiteui", false);
        white_rounded = load("whiteui-rounded");
        white_rounded_left = load("whiteui-rounded-left");
        white_rounded_right = load("whiteui-rounded-right");
        white_knob = load("whiteui-knob");

        var trd = (TextureRegionDrawable) white;
        main = trd.tint(Palette.main);
        darkmain = trd.tint(Palette.darkmain);
        active = trd.tint(Palette.active);

        var npd = (NinePatchDrawable) white_rounded;
        main_rounded = npd.tint(Palette.main);
        darkmain_rounded = npd.tint(Palette.darkmain);
        active_rounded = npd.tint(Palette.active);

        var knb = (NinePatchDrawable) white_knob;
        main_knob = knb.tint(Palette.main);
        darkmain_knob = knb.tint(Palette.darkmain);
        active_knob = knb.tint(Palette.active);

        alpha_chan = load("alpha-chan");
        alpha_chan_dizzy = load("alpha-chan-dizzy");

        underline = load("underline");
        underline_red = load("underline-red");
        cursor = load("cursor");
        selection = load("selection");

        corners = load("corners");
        gray = trd.tint(0f, 0f, 0f, .5f);
        color_blob = load("color-blob");
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
}