package dark.ui;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.func.Cons;
import arc.input.KeyCode;
import arc.math.Interp;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.style.Drawable;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Align;
import dark.ui.dialogs.*;
import dark.ui.fragments.ColorWheel;
import dark.ui.fragments.HudFragment;

import static arc.Core.input;
import static arc.Core.scene;

public class UI implements ApplicationListener {

    public final WidgetGroup hud = new WidgetGroup();

    public final HudFragment hudFragment = new HudFragment();
    public final ColorWheel colorWheel = new ColorWheel();

    public MenuDialog menu;
    public SettingsDialog settings;

    public PaletteDialog palette;
    public ResizeDialog resize;

    public Table toast;

    @Override
    public void init() {
        input.addProcessor(scene);
        scene.add(hud);

        hud.setFillParent(true);
        hud.touchable = Touchable.childrenOnly;

        hudFragment.build(hud);
        colorWheel.build(hud);

        menu = new MenuDialog();
        settings = new SettingsDialog();

        palette = new PaletteDialog();
        resize = new ResizeDialog();
    }

    @Override
    public void update() {
        scene.act();
        scene.draw();

        if (input.keyTap(KeyCode.escape) && !scene.hasDialog() && !menu.isShown()) menu.show();

        if ((input.keyTap(KeyCode.mouseLeft) || input.keyTap(KeyCode.mouseRight)) && scene.hasField())
            if (!(scene.hit(input.mouseX(), input.mouseY(), true) instanceof TextField))
                scene.setKeyboardFocus(null);
    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width, height);
    }

    public void showFileChooser(String title, boolean open, String extension, Cons<Fi> cons) {
        new FileChooser(title, open, extension, cons).show();
    }

    public void showInfoToast(Drawable icon, String text) {
        Sounds.play(Sounds.message);

        if (toast != null) toast.remove();

        toast = scene.table();
        toast.table(Drawables.main_rounded, table -> {
            table.image(icon).color(Palette.active);
            ;
            table.add(text).wrap().size(280f, 32f).labelAlign(Align.center);
        });

        toast.color.a(0f);
        toast.setTranslation(0, 48f);
        toast.actions(
                Actions.parallel(Actions.alpha(1f, .5f, Interp.fastSlow), Actions.translateBy(0, -48f, .5f, Interp.fastSlow)),
                Actions.delay(1f),
                Actions.parallel(Actions.alpha(0f, .5f, Interp.slowFast), Actions.translateBy(0, -48f, .5f, Interp.slowFast)),
                Actions.remove());
    }
}