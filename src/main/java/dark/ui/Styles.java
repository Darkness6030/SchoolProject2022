package dark.ui;

import arc.graphics.Color;
import arc.scene.ui.Button.ButtonStyle;
import arc.scene.ui.CheckBox.CheckBoxStyle;
import arc.scene.ui.Dialog.DialogStyle;
import arc.scene.ui.ImageButton.ImageButtonStyle;
import arc.scene.ui.Label.LabelStyle;
import arc.scene.ui.Slider.SliderStyle;
import arc.scene.ui.TextButton.TextButtonStyle;

import static arc.Core.*;
import static dark.ui.Textures.*;

public class Styles {

    public static ButtonStyle defb;
    public static TextButtonStyle deft, checkt;
    public static ImageButtonStyle defi;

    public static LabelStyle defl;
    public static CheckBoxStyle defc;
    public static SliderStyle defs;

    public static DialogStyle defd;

    public static void load() {
        scene.addStyle(ButtonStyle.class, defb = new ButtonStyle());

        scene.addStyle(TextButtonStyle.class, deft = new TextButtonStyle() {{
            font = Fonts.def;
            fontColor = Color.white;
            downFontColor = Palette.active;
            overFontColor = Color.lightGray;
        }});

        checkt = new TextButtonStyle(deft) {{
            downFontColor = Color.gray;
            checkedFontColor = Palette.active;
        }};

        scene.addStyle(ImageButtonStyle.class, defi = new ImageButtonStyle());

        scene.addStyle(LabelStyle.class, defl = new LabelStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(ButtonStyle.class, defc = new CheckBoxStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(SliderStyle.class, defs = new SliderStyle() {{
            background = slider_back;
            knob = slider_knob;
            knobOver = slider_knob_over;
            knobDown = slider_knob_down;
        }});

        scene.addStyle(DialogStyle.class, defd = new DialogStyle() {{
            stageBackground = Textures.black;
            titleFont = Fonts.def;
            titleFontColor = Palette.active;
        }});
    }
}