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

    public static ButtonStyle button;
    public static TextButtonStyle textButton, textButtonCheck;
    public static ImageButtonStyle imageButton, imageButtonCheck, emptyImageButton, alpha, layer, visible;

    public static LabelStyle label;
    public static ScrollPaneStyle scroll;
    public static SliderStyle slider;
    public static DialogStyle dialog;

    public static CheckBoxStyle checkBoxStyle;
    public static TextFieldStyle fieldStyle;

    public static void load() {
        scene.addStyle(ButtonStyle.class, button = new ButtonStyle());

        scene.addStyle(TextButtonStyle.class, textButton = new TextButtonStyle() {{
            font = Fonts.def;

            up = Drawables.main_rounded;
            over = Drawables.darkmain_rounded;
            down = Drawables.active_rounded;

            disabled = Drawables.darkmain_rounded;
            disabledFontColor = Color.gray;
            fontColor = Color.white;
        }});

        textButtonCheck = new TextButtonStyle() {{
            font = Fonts.def;

            up = Drawables.main_rounded;
            over = Drawables.darkmain_rounded;
            down = Drawables.active_rounded;
            checked = Drawables.active_rounded;
        }};

        scene.addStyle(ImageButtonStyle.class, imageButton = new ImageButtonStyle() {{
            up = Drawables.main_rounded;
            over = Drawables.darkmain_rounded;
            down = Drawables.active_rounded;

            disabled = Drawables.darkmain_rounded;
            imageDisabledColor = Color.gray;
            imageUpColor = Color.white;
        }});

        imageButtonCheck = new ImageButtonStyle() {{
            up = Drawables.main_rounded;
            over = Drawables.darkmain_rounded;
            down = Drawables.active_rounded;
            checked = Drawables.active_rounded;
        }};

        emptyImageButton = new ImageButtonStyle();

        alpha = new ImageButtonStyle() {{
            over = Drawables.darkmain_rounded;
            down = Drawables.active_rounded;

            imageUp = Drawables.alpha_chan;
            imageDown = Drawables.alpha_chan_dizzy;
            imageChecked = Drawables.alpha_chan_dizzy;
        }};

        layer = new ImageButtonStyle() {{
            over = Drawables.darkmain;
            down = Drawables.active;
            checked = Drawables.active;
        }};

        visible = new ImageButtonStyle() {{
            up = Drawables.main_rounded;
            over = Drawables.darkmain_rounded;

            imageUp = Icons.eyeOpen;
            imageDown = Icons.eyeClosed;
            imageChecked = Icons.eyeClosed;
        }};

        scene.addStyle(LabelStyle.class, label = new LabelStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(ScrollPaneStyle.class, scroll = new ScrollPaneStyle() {{
            vScrollKnob = Drawables.active_knob;
            hScrollKnob = Drawables.active_knob;
        }});

        scene.addStyle(SliderStyle.class, slider = new SliderStyle() {{
            background = Drawables.empty;
            knobBefore = Drawables.active_knob;
            knobAfter = Drawables.darkmain_knob;

            knob = Drawables.main_rounded;
            knobOver = Drawables.darkmain_rounded;
            knobDown = Drawables.active_rounded;
        }});

        scene.addStyle(DialogStyle.class, dialog = new DialogStyle() {{
            titleFont = Fonts.def;

            titleFontColor = Palette.active;
            background = Drawables.main_rounded;
            stageBackground = Drawables.gray;
        }});

        // todo старые стили, переделать/удалить
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
    }
}