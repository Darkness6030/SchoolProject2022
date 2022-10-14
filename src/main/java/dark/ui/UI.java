package dark.ui;

import arc.ApplicationListener;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.WidgetGroup;
import dark.ui.dialogs.NewCanvasDialog;
import dark.ui.fragments.ColorWheel;
import dark.ui.fragments.HudFragment;

import static arc.Core.*;

public class UI implements ApplicationListener {

    public final WidgetGroup hud = new WidgetGroup();

    public final HudFragment hudfrag = new HudFragment();
    public final ColorWheel wheelfrag = new ColorWheel();

    public final NewCanvasDialog canvasdial = new NewCanvasDialog();

    public void load() {
        input.addProcessor(scene);
        scene.add(hud);

        hud.setFillParent(true);
        hud.touchable = Touchable.childrenOnly;

        hudfrag.build(hud);
        wheelfrag.build(hud);
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
}