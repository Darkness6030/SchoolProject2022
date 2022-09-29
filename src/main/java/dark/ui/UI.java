package dark.ui;

import arc.scene.event.Touchable;
import arc.scene.ui.Button.ButtonStyle;
import arc.scene.ui.Label.LabelStyle;
import arc.scene.ui.TextButton.TextButtonStyle;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Log;

import static arc.Core.*;

public class UI {

    public static final WidgetGroup hud = new WidgetGroup();

    public static void load() {
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
                pad.defaults().size(64f).pad(8f);

                pad.button("text because there is no icons bruh", () -> Log.infoTag("UI", "First button pressed"));
                pad.button("TODO add Icon because text buttons go brrr", () -> Log.infoTag("UI", "Second button pressed"));
            }).height(80f);
        });

        hud.fill(cont -> {
            cont.add("Anuk sucks");
        });
    }
}
