package dark.ui;

import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.TextureRegionDrawable;

import static arc.Core.*;

public class Textures {

    public static TextureRegionDrawable alpha_bg, alphaaaa, sliderBack, sliderKnob, sliderKnobOver, sliderKnobDown;

    public static void load() {
        alpha_bg = load("alpha-bg");
        alphaaaa = load("alphaaaa");
        sliderBack = load("slider-back.9");
        sliderKnob = load("slider-knob");
        sliderKnobOver = load("slider-knob-over");
        sliderKnobDown = load("slider-knob-down");
    }

    public static TextureRegionDrawable load(String name) {
        TextureRegion region = new TextureRegion(new Texture("sprites/" + name + ".png"));
        atlas.addRegion(name, region);
        return new TextureRegionDrawable(region);
    }
}