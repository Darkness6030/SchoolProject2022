package dark.ui;

import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.graphics.g2d.TextureAtlas.AtlasRegion;
import arc.scene.style.*;
import arc.util.serialization.JsonValue;

import static arc.Core.*;
import static dark.Main.reader;

public class Drawables {

    public static JsonValue splits;
    public static Drawable

    white, white_rounded, circle, corners, scroll_knob,

    main, darkmain, active,

    main_rounded, darkmain_rounded, active_rounded,

    gray, error

    ;

    public static Drawable
            color_blob,
            button,  
            underline, underline_red,
            check_on, check_off, check_over, check_on_over, check_disabled, check_on_disabled,
            slider_back, slider_knob, slider_knob_over, slider_knob_down,
            alpha_chan, alpha_chan_dizzy,
            cursor, selection;

    public static void load() {
        splits = reader.parse(files.internal("sprites/splits.json"));

        white = load("whiteui", false);
        white_rounded = load("whiteui-rounded", true);
        circle = load("circle", false);
        corners = load("corners", true);
        scroll_knob = load("scroll-knob", true);

        var trd = (TextureRegionDrawable) white;
        main = trd.tint(Palette.main);
        darkmain = trd.tint(Palette.darkmain);
        active = trd.tint(Palette.active);

        var npd = (NinePatchDrawable) white_rounded;
        main_rounded = npd.tint(Palette.main);
        darkmain_rounded = npd.tint(Palette.darkmain);
        active_rounded = npd.tint(Palette.active);

        gray = trd.tint(0f, 0f, 0f, .5f);
        error = load("error");

        color_blob = load("color-blob");

        button = load("button");

        underline = load("underline");
        underline_red = load("underline-red");

        check_on = load("check-on");
        check_off = load("check-off");
        check_over = load("check-over");
        check_on_over = load("check-on-over");
        check_disabled = load("check-disabled");
        check_on_disabled = load("check-on-disabled");

        slider_back = load("slider-back");
        slider_knob = load("slider-knob");
        slider_knob_over = load("slider-knob-over");
        slider_knob_down = load("slider-knob-down");

        alpha_chan = load("alpha-chan");
        alpha_chan_dizzy = load("alpha-chan-dizzy");

        cursor = load("cursor");
        selection = load("selection");

        atlas.setErrorRegion("error");
    }

    public static AtlasRegion loadRegion(String name, boolean linear) {
        Texture texture = new Texture("sprites/" + name + ".png");
        if (linear) texture.setFilter(TextureFilter.linear); // for better experience

        var region = atlas.addRegion(name, texture, 0, 0, texture.width, texture.height);
        if (splits.has(name)) region.splits = splits.get(name).asIntArray();

        return region;
    }

    public static Drawable load(String name, boolean linear) {
        loadRegion(name, linear);
        return atlas.drawable(name);
    }

    public static Drawable load(String name) {
        return load(name, true);
    }
}