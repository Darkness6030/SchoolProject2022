package dark.ui;

import arc.ApplicationListener;
import arc.assets.Loadable;
import arc.scene.event.Touchable;
import arc.scene.ui.Button.ButtonStyle;
import arc.scene.ui.Label.LabelStyle;
import arc.scene.ui.TextButton.TextButtonStyle;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Log;

import static arc.Core.*;

public class UI implements ApplicationListener, Loadable {

    public final WidgetGroup hud = new WidgetGroup();

    @Override
    public void loadSync() {
        scene.addStyle(TextButtonStyle.class, new TextButtonStyle() {{
            font = Fonts.def;
        }});
        scene.addStyle(ButtonStyle.class, new ButtonStyle());
        scene.addStyle(LabelStyle.class, new LabelStyle() {{
            font = Fonts.def;
        }});

        hud.setFillParent(true);
        hud.touchable = Touchable.childrenOnly;
        scene.add(hud);

        hud.fill(cont -> {
            cont.name = "Menu Bar";
            cont.top();

            cont.table(pad -> {
                pad.defaults().size(32f).pad(4f);

                pad.button(String.valueOf(Icons.pencil), () -> Log.infoTag("UI", "Pencil selected."));
                pad.button(String.valueOf(Icons.pick), () -> Log.infoTag("UI", "Pick selected."));
                pad.button(String.valueOf(Icons.eraser), () -> Log.infoTag("UI", "Eraser selected."));
            }).height(40f).fillX();
        });
    }

    @Override
    public void update(){
        scene.act();
        scene.draw();
    }

    @Override
    public void resize(int width, int height){
        int[] insets = graphics.getSafeInsets();
        scene.marginLeft = insets[0];
        scene.marginRight = insets[1];
        scene.marginTop = insets[2];
        scene.marginBottom = insets[3];

        scene.resize(width, height);
    }
}
