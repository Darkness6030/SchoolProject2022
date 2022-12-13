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
        Tooltips.getInstance().textProvider = text -> new Tooltip(table -> table.background(Drawables.black).margin(4f).add(text));

        Fonts.load();
        Palette.load();

        load(Drawables.class, Drawables::load);
        load(Styles.class, Styles::load);
        load(Icons.class, Icons::load);
        load(UI.class, ui::load);

        assets.finishLoading();

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