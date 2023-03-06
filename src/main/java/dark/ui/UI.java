package dark.ui;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.func.Cons;
import arc.input.KeyCode;
import arc.math.Interp;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.style.Drawable;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Align;
import dark.ui.dialogs.*;
import dark.ui.fragments.ColorWheel;
import dark.ui.fragments.HudFragment;

import static arc.Core.*;

public class UI implements ApplicationListener {

    public final WidgetGroup hud = new WidgetGroup();

    public final HudFragment hudFragment = new HudFragment();
    public final ColorWheel colorWheel = new ColorWheel();

    public MenuDialog menu;
    public SettingsDialog settings;

    public PaletteDialog palette;
    public ResizeDialog resize;

    public Table toast;

    public void load() {
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

        if (input.keyDown(KeyCode.escape) && !scene.hasDialog()) menu.show();
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

        var table = new Table(Drawables.button);
        table.image(icon);
        table.add(text).wrap().size(280f, 32f).get().setAlignment(Align.center, Align.center);
        table.pack();

        toast = scene.table();
        toast.top().add(table);

        toast.setTranslation(0, table.getPrefHeight());
        toast.actions(
                Actions.translateBy(0, -table.getPrefHeight(), 1f, Interp.fade),
                Actions.delay(2.5f),
                Actions.run(() -> toast.actions(Actions.translateBy(0, table.getPrefHeight(), 1f, Interp.fade), Actions.remove())));
    }
}