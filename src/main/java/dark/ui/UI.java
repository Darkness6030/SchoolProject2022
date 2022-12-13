package dark.ui;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.func.Cons;
import arc.math.Interp;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import dark.ui.dialogs.*;
import dark.ui.fragments.ColorWheel;
import dark.ui.fragments.HudFragment;

import static arc.Core.*;

public class UI implements ApplicationListener {

    public final WidgetGroup hud = new WidgetGroup();

    public final HudFragment hudFragment = new HudFragment();
    public final ColorWheel colorWheel = new ColorWheel();

    public MenuDialog menuDialog;
    public NewCanvasDialog canvasDialog;
    public ColorPickerDialog pickerDialog;

    public void load() {
        input.addProcessor(scene);
        scene.add(hud);

        hud.setFillParent(true);
        hud.touchable = Touchable.childrenOnly;

        hudFragment.build(hud);
        colorWheel.build(hud);

        menuDialog = new MenuDialog();
        canvasDialog = new NewCanvasDialog();
        pickerDialog = new ColorPickerDialog();
    }

    @Override
    public void update() {
        scene.act();
        scene.draw();
    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width, height);
    }

    public void showFileChooser(boolean open, String title, String extension, Cons<Fi> cons) {
        new FileChooser(title, fi -> fi.extEquals(extension), open, cons).show();
    }

    public void showInfoFade(String text, float duration) {
        new Table(table -> {
            table.setFillParent(true);
            table.touchable = Touchable.disabled;

            table.actions(Actions.fadeOut(duration, Interp.fade), Actions.remove());
            table.top().add(text).padTop(10f);

            scene.add(table);
        });
    }
}