package dark.ui;

import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.TextureRegionDrawable;

import static arc.Core.*;

public class Textures {

    public static TextureRegionDrawable alpha_bg, alpha_bg_line, white_pane, slider_back, slider_knob, slider_knob_over, slider_knob_down;

    public static void load() {
        alpha_bg = load("alpha-bg");
        alpha_bg_line = load("alpha-bg-line");
        white_pane = load("white-pane.9");
        slider_back = load("slider-back.9");
        slider_knob = load("slider-knob");
        slider_knob_over = load("slider-knob-over");
        slider_knob_down = load("slider-knob-down");
    }

    public static TextureRegionDrawable load(String name) {
        TextureRegion region = new TextureRegion(new Texture("sprites/" + name + ".png"));
        atlas.addRegion(name, region);
        return new TextureRegionDrawable(region);
    }
}