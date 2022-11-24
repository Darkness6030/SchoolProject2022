package dark;

import arc.ApplicationCore;
import arc.assets.AssetManager;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.graphics.g2d.TextureAtlas;
import arc.scene.Scene;
import arc.scene.ui.Tooltip.Tooltips;
import arc.util.*;
import dark.editor.Editor;
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

        bundle = I18NBundle.createBundle(files.internal("bundles/bundle"), Locale.ENGLISH); // TODO выбор языка

        add(editor = new Editor());
        add(ui = new UI());

        Tooltips.getInstance().animations = true;

        Fonts.load();
        Palette.load();

        load(Textures.class, Textures::load);
        load(Styles.class, Styles::load);
        load(UI.class, ui::load);

        assets.finishLoading();

        Main.info("Total time to load: @ms", Time.elapsed());
        Main.info("Initialized Application.");
    }

    public void load(Class<?> type, Runnable load) {
        assets.loadRun(type.getSimpleName(), type, () -> {}, load);
    }

    @Override
    public void dispose() {
        super.dispose();
        Main.info("Disposed.");
    }
}