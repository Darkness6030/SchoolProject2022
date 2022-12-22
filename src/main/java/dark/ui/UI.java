package dark.ui;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.func.Cons;
import arc.graphics.Color;
import arc.math.Interp;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
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

    public void showInfoFade(String text) {
        scene.table(table -> {
           table.touchable = Touchable.disabled;

            table.center().add(text);

            table.moveBy(0f, 32f);
            table.setColor(new Color(1f, 1f, 1f, 0f));

            table.actions(Actions.parallel(Actions.moveBy(0f, -32f, 0.5f, Interp.fade), Actions.fadeIn(0.5f, Interp.fade)), Actions.delay(1f), Actions.parallel(Actions.moveBy(0f, -32f, 0.5f, Interp.fade), Actions.fadeOut(0.5f, Interp.fade)), Actions.remove());
        });
    }
}