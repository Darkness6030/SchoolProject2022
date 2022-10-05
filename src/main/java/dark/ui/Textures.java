package dark.ui;

import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.TextureRegionDrawable;

import static arc.Core.*;

public class Textures {

    public static TextureRegionDrawable aplha_bg;

    public static void load() {
        aplha_bg = load("alpha-bg");
    }

    public static TextureRegionDrawable load(String name) {
        TextureRegion region = new TextureRegion(new Texture("sprites/" + name + ".png"));
        atlas.addRegion(name, region);
        return new TextureRegionDrawable(region);
    }
}