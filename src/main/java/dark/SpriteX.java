package dark;

import arc.ApplicationListener;
import arc.Core;
import arc.assets.AssetManager;
import arc.graphics.Color;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.scene.Scene;
import arc.util.Log;
import arc.util.Time;
import dark.ui.Fonts;
import dark.ui.UI;

import static arc.Core.*;

public class SpriteX implements ApplicationListener {

    public static boolean finished;

    @Override
    public void init() {
        Time.mark();

        batch = new SortedSpriteBatch();
        scene = new Scene();
        assets = new AssetManager();

        Fonts.load();            

        Log.infoTag("APP", "Initialized");
    }

    @Override
    public void dispose() {
        Log.infoTag("APP", "Disposed");
    }

    @Override
    public void update() {
        if (finished) {
            Core.graphics.clear(Color.blue);
            Core.scene.draw();
        } else if (assets.update(50)) {
            UI.load();

            finished = true;
            Log.info("Total time to load: @ms", Time.elapsed());
        }
    }
}
