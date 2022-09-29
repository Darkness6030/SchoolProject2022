package dark.ui;

import static arc.Core.*;

import arc.scene.ui.Button.ButtonStyle;
import arc.scene.ui.CheckBox.CheckBoxStyle;
import arc.scene.ui.ImageButton.ImageButtonStyle;
import arc.scene.ui.Label.LabelStyle;
import arc.scene.ui.TextButton.TextButtonStyle;

public class Styles {

    public static ButtonStyle defb;
    public static TextButtonStyle deft;
    public static ImageButtonStyle defi;

    public static LabelStyle defl;
    public static CheckBoxStyle defc;

    public static void load() {
        scene.addStyle(ButtonStyle.class, defb = new ButtonStyle());

        scene.addStyle(TextButtonStyle.class, deft = new TextButtonStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(ButtonStyle.class, defi = new ImageButtonStyle());

        scene.addStyle(LabelStyle.class, defl = new LabelStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(ButtonStyle.class, defc = new CheckBoxStyle() {{
            font = Fonts.def;
        }});
    }
}
