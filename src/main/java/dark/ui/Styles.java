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
    public static ImageButtonStyle imageNoneStyle, imageButtonStyle, checkImageButtonStyle, layerImageButtonStyle, alphaStyle;

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
            disabledFontColor = Color.gray;

            down = Drawables.button_down;
            up = Drawables.button;
            over = Drawables.button_over;
            disabled = Drawables.button_disabled;
        }});

        checkTextButtonStyle = new TextButtonStyle() {{
            font = Fonts.def;
            fontColor = Color.white;
            disabledFontColor = Color.gray;

            checked = Drawables.flatDown;
            down = Drawables.flatDown;
            up = Drawables.black;
            over = Drawables.gray;
            disabled = Drawables.black;
        }};

        imageNoneStyle = new ImageButtonStyle();

        scene.addStyle(ImageButtonStyle.class, imageButtonStyle = new ImageButtonStyle() {{
            down = Drawables.button_down;
            up = Drawables.button;
            over = Drawables.button_over;
            disabled = Drawables.button_disabled;
        }});

        checkImageButtonStyle = new ImageButtonStyle() {{
            down = Drawables.flatDown;
            checked = Drawables.flatDown;
            up = Drawables.black;
            over = Drawables.gray;
        }};

        layerImageButtonStyle = new ImageButtonStyle() {{
            down = Drawables.flatDown;
            checked = Drawables.flatDown;
            up = Drawables.black;
            over = Drawables.gray;
        }};

        alphaStyle = new ImageButtonStyle() {{
            imageUp = Drawables.alpha_chan;
            imageDown = Drawables.alpha_chan_dizzy;
            imageChecked = Drawables.alpha_chan_dizzy;
        }};

        scene.addStyle(LabelStyle.class, labelStyle = new LabelStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(CheckBoxStyle.class, checkBoxStyle = new CheckBoxStyle() {{
            font = Fonts.def;
            fontColor = Color.white;
            disabledFontColor = Color.gray;

            checkboxOn = Drawables.check_on;
            checkboxOff = Drawables.check_off;
            checkboxOnOver = Drawables.check_on_over;
            checkboxOver = Drawables.check_over;
            checkboxOnDisabled = Drawables.check_on_disabled;
            checkboxOffDisabled = Drawables.check_disabled;
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
            messageFont = Fonts.def;
            messageFontColor = Color.gray;
            disabledFontColor = Color.gray;

            cursor = Drawables.cursor;
            selection = Drawables.selection;

            background = Drawables.underline;
            invalidBackground = Drawables.underline_red;
        }});

        scene.addStyle(ScrollPaneStyle.class, scrollPaneStyle = new ScrollPaneStyle());

        scene.addStyle(DialogStyle.class, dialogStyle = new DialogStyle() {{
            stageBackground = Drawables.black;
            titleFont = Fonts.def;
            titleFontColor = Palette.active;
        }});
    }
}