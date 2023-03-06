package dark;

import arc.ApplicationCore;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.graphics.g2d.TextureAtlas;
import arc.scene.Scene;
import arc.scene.ui.Tooltip;
import arc.scene.ui.Tooltip.Tooltips;
import arc.util.I18NBundle;
import arc.util.Log;
import arc.util.Time;
import dark.ui.*;

import static arc.Core.*;
import static dark.Main.*;

import java.util.Locale;

public class SpriteX extends ApplicationCore {

    @Override
    public void setup() {
        Time.mark();

        batch = new SortedSpriteBatch();
        scene = new Scene();
        atlas = TextureAtlas.blankAtlas();

        settings.defaults("locale", "en", "sfxvol", 100); // TODO Дарк, ну это для чего-то нужно, разберись
        settings.setAppName("SpriteX");
        settings.load();

        bundle = I18NBundle.createBundle(files.internal("bundles/bundle"), Locale.ENGLISH); // TODO выбор языка

        Tooltips.getInstance().animations = true;
        Tooltips.getInstance().textProvider = text -> new Tooltip(table -> table.background(Drawables.gray2).margin(4f).add(text));

        Fonts.load();
        Drawables.load();
        Styles.load();
        
        Palette.load();
        Sounds.load();
        Icons.load();

        add(editor);
        add(ui);

        editor.load();
        ui.load();

        Log.info("Total time to load: @ms", Time.elapsed());
    }

    @Override
    public void resize(int width, int height) {
        ui.resize(width, height);
    }
}
