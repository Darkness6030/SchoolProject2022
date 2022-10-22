package dark.ui;

import arc.ApplicationListener;
import arc.input.KeyCode;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.WidgetGroup;
import dark.ui.dialogs.MenuDialog;
import dark.ui.dialogs.NewCanvasDialog;
import dark.ui.fragments.ColorWheel;
import dark.ui.fragments.HudFragment;

import static arc.Core.*;

public class UI implements ApplicationListener {

    public final WidgetGroup hud = new WidgetGroup();

    public final HudFragment hudFragment = new HudFragment();
    public final ColorWheel colorWheel = new ColorWheel();

    public MenuDialog menuDialog;
    public NewCanvasDialog canvasDialog;

    public void load() {
        input.addProcessor(scene);
        scene.add(hud);

        hud.setFillParent(true);
        hud.touchable = Touchable.childrenOnly;

        hudFragment.build(hud);
        colorWheel.build(hud);

        menuDialog = new MenuDialog();
        canvasDialog = new NewCanvasDialog();
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