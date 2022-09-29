package dark;

import arc.ApplicationCore;
import arc.ApplicationListener;
import arc.assets.AssetManager;
import arc.assets.Loadable;
import arc.graphics.Color;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.scene.Scene;
import arc.util.Log;
import arc.util.Time;
import dark.ui.Fonts;
import dark.ui.UI;

import static arc.Core.*;
import static dark.Main.*;

public class SpriteX extends ApplicationCore {

    public boolean finished;

    @Override
    public void setup() {
        Time.mark();

        batch = new SortedSpriteBatch();
        input.addProcessor(scene = new Scene());
        assets = new AssetManager();

        Fonts.load();
    
        add(ui = new UI());

        Log.infoTag("APP", "Initialized");
    }

    @Override
    public void add(ApplicationListener module) {
        super.add(module);
        if (module instanceof Loadable l) assets.load(l);
    }

    @Override
    public void update() {
        if (finished) {
            graphics.clear(Color.blue);
            super.update();
        } else if (assets.update(50)) {
            finished = true;
            Log.info("[APP] Total time to load: @ms", Time.elapsed());
        }
    }

    @Override
    public void dispose() {
        Log.infoTag("APP", "Disposed");
    }
}
