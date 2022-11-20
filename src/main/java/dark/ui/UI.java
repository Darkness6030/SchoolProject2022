package dark.ui;

import arc.ApplicationListener;
import arc.input.KeyCode;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.WidgetGroup;
import dark.ui.dialogs.*;
import dark.ui.fragments.*;

import static arc.Core.*;
import static dark.Main.editor;

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

        if (input.keyTap(KeyCode.escape) && !scene.hasMouse() && !scene.hasDialog()) menuDialog.show();
    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width, height);
    }
}