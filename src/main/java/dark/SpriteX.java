package dark;

import arc.ApplicationCore;
import arc.assets.AssetManager;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.scene.Scene;
import arc.util.Time;
import dark.ui.*;

import static arc.Core.*;
import static dark.Main.ui;

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

    public void load(Class<?> type, Runnable load) {
        assets.loadRun(type.getSimpleName(), type, () -> {}, load);
    }

    @Override
    public void update() {
        if (!loaded && assets.update(50)) {
            loaded = true;
            Main.info("Total time to load: @ms", Time.elapsed());
        }

        super.update();
    }

    @Override
    public void dispose() {
        Main.info("Disposed.");
    }
}
