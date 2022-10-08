package dark.ui;

import arc.graphics.Color;
import arc.scene.ui.Button.ButtonStyle;
import arc.scene.ui.CheckBox.CheckBoxStyle;
import arc.scene.ui.ImageButton.ImageButtonStyle;
import arc.scene.ui.Label.LabelStyle;
import arc.scene.ui.Slider.SliderStyle;
import arc.scene.ui.TextButton.TextButtonStyle;

import static arc.Core.*;
import static dark.ui.Textures.*;

public class Styles {

    public static ButtonStyle defb;
    public static TextButtonStyle deft;
    public static ImageButtonStyle defi;

    public static LabelStyle defl;
    public static CheckBoxStyle defc;

    public static SliderStyle defs;

    public static void load() {
        scene.addStyle(ButtonStyle.class, defb = new ButtonStyle());

        scene.addStyle(TextButtonStyle.class, deft = new TextButtonStyle() {{
            font = Fonts.def;
            fontColor = Color.white;
            downFontColor = Color.gray;
            overFontColor = Color.lightGray;
            checkedFontColor = Color.yellow;
        }});

        scene.addStyle(ButtonStyle.class, defi = new ImageButtonStyle());

        scene.addStyle(LabelStyle.class, defl = new LabelStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(ButtonStyle.class, defc = new CheckBoxStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(SliderStyle.class, defs = new SliderStyle() {{
            background = sliderBack;
            knob = alphaaaa;
            knobOver = alphaaaa;
            knobDown = alphaaaa;
        }});
    }
}
