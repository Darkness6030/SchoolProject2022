package dark.ui;

import arc.graphics.Color;
import arc.scene.ui.Button.ButtonStyle;
import arc.scene.ui.CheckBox.CheckBoxStyle;
import arc.scene.ui.Dialog.DialogStyle;
import arc.scene.ui.ImageButton.ImageButtonStyle;
import arc.scene.ui.Label.LabelStyle;
import arc.scene.ui.ScrollPane.ScrollPaneStyle;
import arc.scene.ui.Slider.SliderStyle;
import arc.scene.ui.TextButton.TextButtonStyle;
import arc.scene.ui.TextField.TextFieldStyle;

import static arc.Core.scene;

public class Styles {

    public static ButtonStyle buttonStyle;
    public static TextButtonStyle textButtonStyle, checkTextButtonStyle;
    public static ImageButtonStyle imageButtonStyle, checkImageButtonStyle, layerImageButtonStyle, alphaStyle;

    public static LabelStyle labelStyle;
    public static CheckBoxStyle checkBoxStyle;
    public static SliderStyle sliderStyle;
    public static TextFieldStyle fieldStyle;
    public static ScrollPaneStyle scrollPaneStyle;

    public static DialogStyle dialogStyle;

    public static void load() {
        scene.addStyle(ButtonStyle.class, buttonStyle = new ButtonStyle());

        scene.addStyle(TextButtonStyle.class, textButtonStyle = new TextButtonStyle() {{
            font = Fonts.def;
            fontColor = Color.white;
            downFontColor = Palette.active;
            overFontColor = Color.lightGray;
        }});

        checkTextButtonStyle = new TextButtonStyle(textButtonStyle) {{
            font = Fonts.def;
            fontColor = Color.white;

            checked = Drawables.flatdown;
            down = Drawables.flatdown;
            up = Drawables.blackui;
            over = Drawables.grayui;
            disabled = Drawables.blackui;

            disabledFontColor = Color.gray;

            downFontColor = Color.gray;
            checkedFontColor = Palette.active;
        }};

        scene.addStyle(ImageButtonStyle.class, imageButtonStyle = new ImageButtonStyle());

        checkImageButtonStyle = new ImageButtonStyle() {{
            down = Drawables.flatdown;
            checked = Drawables.flatdown;
            up = Drawables.blackui;
            over = Drawables.grayui;

            imageDownColor = Color.gray;
            imageUpColor = Color.white;
            imageCheckedColor = Palette.active;
        }};

        layerImageButtonStyle = new ImageButtonStyle() {{
            down = Drawables.flatdown;
            checked = Drawables.flatdown;
            up = Drawables.blackui;
            over = Drawables.grayui;
        }};

        alphaStyle = new ImageButtonStyle() {{
            imageUp = Drawables.alpha_chan;
            imageDown = Drawables.alpha_chan_dizzy;
            imageChecked = Drawables.alpha_chan_dizzy;
        }};

        scene.addStyle(LabelStyle.class, labelStyle = new LabelStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(ButtonStyle.class, checkBoxStyle = new CheckBoxStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(SliderStyle.class, sliderStyle = new SliderStyle() {{
            background = Drawables.slider_back;
            knob = Drawables.slider_knob;
            knobOver = Drawables.slider_knob_over;
            knobDown = Drawables.slider_knob_down;
        }});

        scene.addStyle(TextFieldStyle.class, fieldStyle = new TextFieldStyle() {{
            font = Fonts.def;
            fontColor = Color.white;
        }});

        scene.addStyle(ScrollPaneStyle.class, scrollPaneStyle = new ScrollPaneStyle());

        scene.addStyle(DialogStyle.class, dialogStyle = new DialogStyle() {{
            stageBackground = Drawables.blackui;
            titleFont = Fonts.def;
            titleFontColor = Palette.active;
        }});
    }
}