package dark;

import arc.ApplicationCore;
import arc.assets.AssetManager;
import arc.graphics.Color;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.scene.Scene;
import arc.util.Time;
import dark.ui.Fonts;
import dark.ui.Styles;
import dark.ui.UI;

import static arc.Core.*;
import static dark.Main.*;

public class SpriteX extends ApplicationCore {

    public static boolean loaded;

    @Override
    public void setup() {
        Time.mark();

        batch = new SortedSpriteBatch();
        input.addProcessor(scene = new Scene());
        assets = new AssetManager();
        add(ui = new UI());

        Fonts.load();
        load(Styles.class, Styles::load);
        load(UI.class, ui::load);

        Main.info("Initialized.");
    }

    public void load(Class<?> type, Runnable loadsync) {
        assets.loadRun(type.getSimpleName(), type, () -> {}, loadsync);
    }

    @Override
    public void update() {
        if (loaded) {
            graphics.clear(Color.blue);
            super.update();
        } else if (assets.update(50)) {
            loaded = true;
            Main.info("Total time to load: @ms", Time.elapsed());
        }
    }

    @Override
    public void dispose() {
        Main.info("Disposed.");
    }
}
