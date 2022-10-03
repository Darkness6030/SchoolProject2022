package dark.ui;

import arc.ApplicationListener;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.WidgetGroup;
import dark.editor.EditType;
import dark.ui.fragments.ColorWheel;

import static arc.Core.*;

public class UI implements ApplicationListener {

    public final WidgetGroup hud = new WidgetGroup();

    public final ColorWheel wheelfrag = new ColorWheel();

    public void load() {
        input.addProcessor(scene);
        scene.add(hud);

        hud.setFillParent(true);
        hud.touchable = Touchable.childrenOnly;

        hud.fill(cont -> {
            cont.name = "Menu Bar";
            cont.top().left();

            cont.table(pad -> {
                pad.defaults().size(32f).pad(4f);

                for (var type : EditType.values()) type.button(pad);
            }).height(40f).fillX();
        });

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
