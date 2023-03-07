package dark.ui;

import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.graphics.g2d.NinePatch;
import arc.graphics.g2d.TextureAtlas.AtlasRegion;
import arc.scene.style.*;
import arc.util.serialization.JsonValue;

import static arc.Core.*;
import static dark.Main.reader;

public class Drawables {

    public static JsonValue splits;
    public static Drawable

    white, white_rounded, circle, corners,

    main, darkmain, active,

    main_rounded, darkmain_rounded, active_rounded,

    gray1, gray2

    ;

    public static Drawable  error, info_table, flat_down,
            color_blob,
            button, button_disabled, button_down, button_over,
            underline, underline_red,
            sideline, sideline_left, sideline_side,
            check_on, check_off, check_over, check_on_over, check_disabled, check_on_disabled,
            slider_back, slider_knob, slider_knob_over, slider_knob_down,
            scroll_vertical, scroll_horizontal, scroll_knob_vertical_black, scroll_knob_horizontal_black,
            alpha_chan, alpha_chan_dizzy,
            cursor, selection;

    public static void load() {
        splits = reader.parse(files.internal("sprites/splits.json"));

        white = load("whiteui", false);
        white_rounded = load("whiteui-rounded", true);
        circle = load("circle", false);
        corners = load("corners", true);

        var trd = (TextureRegionDrawable) white;
        main = trd.tint(Palette.main);
        darkmain = trd.tint(Palette.darkmain);
        active = trd.tint(Palette.active);

        var npd = (NinePatchDrawable) white_rounded;
        main_rounded = npd.tint(Palette.main);
        darkmain_rounded = npd.tint(Palette.darkmain);
        active_rounded = npd.tint(Palette.active);

        gray1 = trd.tint(.27f, .27f, .27f, 1f);
        gray2 = trd.tint(0f, 0f, 0f, .5f);

        error = load("error");
        info_table = load("info-table");
        flat_down = createFlatDown();

        color_blob = load("color-blob");

        button = load("button");
        button_disabled = load("button-disabled");
        button_down = load("button-down");
        button_over = load("button-over");

        underline = load("underline");
        underline_red = load("underline-red");

        sideline = load("sideline");
        sideline_left = load("sideline-left");
        sideline_side = load("sideline-side");

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

        scroll_vertical = load("scroll-vertical");
        scroll_horizontal = load("scroll-horizontal");
        scroll_knob_vertical_black = load("scroll-knob-vertical-black");
        scroll_knob_horizontal_black = load("scroll-knob-horizontal-black");

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

    public static Drawable createFlatDown() {
        var region = loadRegion("flat-down-base", false);

        var drawable = new ScaledNinePatchDrawable(new NinePatch(region, region.splits[0], region.splits[1], region.splits[2], region.splits[3])) {
            public float getLeftWidth() {
                return 0;
            }

            public float getRightWidth() {
                return 0;
            }

            public float getTopHeight() {
                return 0;
            }

            public float getBottomHeight() {
                return 0;
            }
        };

        drawable.setMinWidth(0);
        drawable.setMinHeight(0);
        drawable.setTopHeight(0);
        drawable.setRightWidth(0);
        drawable.setBottomHeight(0);
        drawable.setLeftWidth(0);
        return drawable;
    }
}