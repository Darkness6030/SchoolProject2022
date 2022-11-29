package dark.ui;

import arc.graphics.Color;
import arc.graphics.Texture;
import arc.graphics.Texture.TextureFilter;
import arc.graphics.g2d.NinePatch;
import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureAtlas.AtlasRegion;
import arc.scene.style.*;
import arc.util.serialization.JsonValue;

import static arc.Core.*;
import static dark.Main.reader;

public class Drawables {

    public static JsonValue splits;
    public static Drawable circle, error, flatdown,
            whiteui, blackui, grayui, color_blob,
            underline, sideline, sideline_left,
            slider_back, slider_knob, slider_knob_over, slider_knob_down,
            alpha_chan, alpha_chan_dizzy;

    public static void load() {
        splits = reader.parse(files.internal("sprites/splits.json"));

        circle = load("circle", false);
        error = load("error");

        flatdown = createFlatDown();

        whiteui = load("whiteui");
        blackui = ((TextureRegionDrawable) whiteui).tint(0f, 0f, 0f, .5f);
        grayui = ((TextureRegionDrawable) whiteui).tint(Color.valueOf("454545"));

        color_blob = load("color-blob");

        underline = load("underline");
        sideline = load("sideline");
        sideline_left = load("sideline-left");

        slider_back = load("slider-back");
        slider_knob = load("slider-knob");
        slider_knob_over = load("slider-knob-over");
        slider_knob_down = load("slider-knob-down");

        alpha_chan = load("alpha-chan");
        alpha_chan_dizzy = load("alpha-chan-dizzy");

        atlas.setErrorRegion("error");
    }

    public static Drawable load(String name) {
        return load(name, true);
    }

    public static Drawable load(String name, boolean linear) {
        loadRegion(name, linear);
        return atlas.drawable(name);
    }

    public static AtlasRegion loadRegion(String name, boolean linear) {
        var texture = new Texture("sprites/" + name + ".png");
        if (linear) texture.setFilter(TextureFilter.linear); // for better experience

        // add texture to atlas and get region to modify splits
        var region = atlas.addRegion(name, texture, 0, 0, texture.width, texture.height);
        if (splits.has(name)) region.splits = splits.get(name).asIntArray();
        return region;
    }

    public static Drawable createFlatDown() {
        var region = loadRegion("flat-down-base", false);

        var drawable = new ScaledNinePatchDrawable(new NinePatch(region, region.splits[0], region.splits[1], region.splits[2], region.splits[3])) {
            public float getLeftWidth() {return 0;}

            public float getRightWidth() {return 0;}

            public float getTopHeight() {return 0;}

            public float getBottomHeight() {return 0;}
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