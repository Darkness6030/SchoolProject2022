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

        settings.defaults("locale", "en", "sfxvol", 100); // TODO locale для локализации, sfxvol для громкости звука
        settings.setAppName("SpriteX");
        settings.load();

        bundle = I18NBundle.createBundle(files.internal("bundles/bundle"), Locale.ENGLISH); // TODO выбор языка

        Tooltips.getInstance().animations = true;
        Tooltips.getInstance().textProvider = text -> new Tooltip(table -> table.background(Drawables.gray).margin(4f).add(text));

        Drawables.load();
        Fonts.load();
        Icons.load();
        Palette.load();
        Sounds.load();
        Styles.load();

        add(editor);
        add(ui);

        Log.info("Total time to load: @ms", Time.elapsed());
    }
}