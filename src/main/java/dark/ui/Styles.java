package dark.ui;

import arc.graphics.Color;
import arc.scene.ui.Button.ButtonStyle;
import arc.scene.ui.CheckBox.CheckBoxStyle;
import arc.scene.ui.Dialog.DialogStyle;
import arc.scene.ui.ImageButton.ImageButtonStyle;
import arc.scene.ui.Label.LabelStyle;
import arc.scene.ui.Slider.SliderStyle;
import arc.scene.ui.TextButton.TextButtonStyle;

import static arc.Core.scene;
import static dark.ui.Textures.*;

public class Styles {

    public static ButtonStyle buttonStyle;
    public static TextButtonStyle textButtonStyle, checkButtonStyle;
    public static ImageButtonStyle imageButtonStyle;

    public static LabelStyle labelStyle;
    public static CheckBoxStyle checkBoxStyle;
    public static SliderStyle sliderStyle;

    public static DialogStyle dialogStyle;

    public static void load() {
        scene.addStyle(ButtonStyle.class, buttonStyle = new ButtonStyle());

        scene.addStyle(TextButtonStyle.class, textButtonStyle = new TextButtonStyle() {{
            font = Fonts.def;
            fontColor = Color.white;
            downFontColor = Palette.active;
            overFontColor = Color.lightGray;
        }});

        checkButtonStyle = new TextButtonStyle(textButtonStyle) {{
            downFontColor = Color.gray;
            checkedFontColor = Palette.active;
        }};

        scene.addStyle(ImageButtonStyle.class, imageButtonStyle = new ImageButtonStyle() {{

        }});

        scene.addStyle(LabelStyle.class, labelStyle = new LabelStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(ButtonStyle.class, checkBoxStyle = new CheckBoxStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(SliderStyle.class, sliderStyle = new SliderStyle() {{
            background = slider_back;
            knob = slider_knob;
            knobOver = slider_knob_over;
            knobDown = slider_knob_down;
        }});

        scene.addStyle(DialogStyle.class, dialogStyle = new DialogStyle() {{
            stageBackground = Textures.black;
            titleFont = Fonts.def;
            titleFontColor = Palette.active;
        }});
    }
}