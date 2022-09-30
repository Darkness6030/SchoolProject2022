package dark.ui;

import arc.ApplicationListener;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.WidgetGroup;
import dark.editor.EditType;

import static arc.Core.*;

public class UI implements ApplicationListener {

    public final WidgetGroup hud = new WidgetGroup();

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
    }

    @Override
    public void update() {
        scene.act();
        scene.draw();
    }

    @Override
    public void resize(int width, int height) {
        int[] insets = graphics.getSafeInsets();
        scene.marginLeft = insets[0];
        scene.marginRight = insets[1];
        scene.marginTop = insets[2];
        scene.marginBottom = insets[3];

        scene.resize(width, height);
    }
}
