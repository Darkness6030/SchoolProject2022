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

    public MenuDialog menuDialog;
    //public SettingsDialog settingsDialog; // TODO

    public PaletteDialog pickerDialog;
    public ResizeDialog canvasDialog;

    public Table lastToast;

    public void load() {
        input.addProcessor(scene);
        scene.add(hud);

        hud.setFillParent(true);
        hud.touchable = Touchable.childrenOnly;

        hudFragment.build(hud);
        colorWheel.build(hud);

        menuDialog = new MenuDialog();
        canvasDialog = new ResizeDialog();
        pickerDialog = new PaletteDialog();
    }

    @Override
    public void update() {
        scene.act();
        scene.draw();

        if (input.keyDown(KeyCode.escape) && !scene.hasDialog())
            menuDialog.show();
    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width, height);
    }

    public void showFileChooser(boolean open, String title, String extension, Cons<Fi> cons) {
        new FileChooser(title, extension, open, cons).show();
    }

    public void showInfoToast(Drawable icon, String text) {
        Sounds.message.play();

        if (lastToast != null) lastToast.remove();

        var table = new Table(Drawables.button);
        table.image(icon);
        table.add(text).wrap().size(280f, 32f).get().setAlignment(Align.center, Align.center);
        table.pack();

        var container = lastToast = scene.table();
        container.top().add(table);

        container.setTranslation(0, table.getPrefHeight());
        container.actions(Actions.translateBy(0, -table.getPrefHeight(), 1f, Interp.fade), Actions.delay(2.5f), Actions.run(() -> container.actions(Actions.translateBy(0, table.getPrefHeight(), 1f, Interp.fade), Actions.remove())));
    }
}