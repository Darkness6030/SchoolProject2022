package dark;

import arc.ApplicationCore;
import arc.assets.AssetManager;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.graphics.g2d.TextureAtlas;
import arc.scene.Scene;
import arc.scene.ui.Tooltip;
import arc.scene.ui.Tooltip.Tooltips;
import arc.util.I18NBundle;
import arc.util.Time;
import dark.ui.*;

import java.util.Locale;

import static arc.Core.*;
import static dark.Main.*;

public class SpriteX extends ApplicationCore {

    @Override
    public void setup() {
        Time.mark();

        batch = new SortedSpriteBatch();
        scene = new Scene();
        assets = new AssetManager();
        atlas = TextureAtlas.blankAtlas();

        settings.defaults("locale", "en", "sfxvol", 100); // TODO
        settings.setAppName("SpriteX");
        settings.load();

        bundle = I18NBundle.createBundle(files.internal("bundles/bundle"), Locale.ENGLISH); // TODO выбор языка

        Tooltips.getInstance().animations = true;
        Tooltips.getInstance().textProvider = text -> new Tooltip(table -> table.background(Drawables.gray2).margin(4f).add(text));

        Fonts.load();
        Palette.load();

        load(Drawables.class, Drawables::load);
        load(Sounds.class, Sounds::load);
        load(Styles.class, Styles::load);
        load(Icons.class, Icons::load);

        assets.finishLoading();

        add(editor);
        add(ui);

        editor.load();
        ui.load();

        info("Total time to load: @ms", Time.elapsed());
        info("Initialized Application.");
    }

    public void load(Class<?> type, Runnable load) {
        assets.loadRun(type.getSimpleName(), type, () -> {}, load);
    }

    @Override
    public void dispose() {
        super.dispose();
        info("Disposed.");
    }
}