package dark;

import arc.ApplicationListener;
import arc.Core;
import arc.assets.AssetManager;
import arc.graphics.Color;
import arc.scene.Scene;
import arc.util.Log;
import dark.ui.Fonts;

import static arc.Core.*;

public class SpriteX implements ApplicationListener {

    @Override
    public void init() {
        scene = new Scene();
        assets = new AssetManager();

        Fonts.load();
        assets.update(50);

        Log.infoTag("SPRITEX", "Initialized");
    }

    @Override
    public void dispose() {
        Log.infoTag("SPRITEX", "Disposed");
    }

    @Override
    public void update() {
        Core.graphics.clear(Color.blue);
        Core.scene.draw();
    }
}
