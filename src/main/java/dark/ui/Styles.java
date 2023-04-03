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

import static arc.Core.*;

public class Styles {

    public static ButtonStyle button;
    public static TextButtonStyle textButton, textButtonCheck, layersTab, historyTab;
    public static ImageButtonStyle imageButton, imageButtonCheck, emptyImageButton, alpha, layer, visible, background;

    public static LabelStyle label;
    public static TextFieldStyle field;
    public static ScrollPaneStyle scroll;
    public static SliderStyle slider;
    public static DialogStyle dialog;
    public static CheckBoxStyle checkbox;

    public static void load() {
        scene.addStyle(ButtonStyle.class, button = new ButtonStyle());

        scene.addStyle(TextButtonStyle.class, textButton = new TextButtonStyle() {{
            font = Fonts.def;
            fontColor = Color.white;

            up = Drawables.main_rounded;
            over = Drawables.darkmain_rounded;
            down = Drawables.active_rounded;

            disabled = Drawables.darkmain_rounded;
            disabledFontColor = Color.gray;
        }});

        textButtonCheck = new TextButtonStyle() {{
            font = Fonts.def;
            fontColor = Color.white;

            up = Drawables.main_rounded;
            over = Drawables.darkmain_rounded;
            down = Drawables.active_rounded;
            checked = Drawables.active_rounded;

            disabled = Drawables.darkmain_rounded;
            disabledFontColor = Color.gray;
        }};

        layersTab = new TextButtonStyle() {{
            font = Fonts.def;
            fontColor = Color.white;

            up = Drawables.main_rounded_left;
            over = Drawables.darkmain_rounded_left;
            down = Drawables.active_rounded_left;
            checked = Drawables.active_rounded_left;
        }};

        historyTab = new TextButtonStyle() {{
            font = Fonts.def;
            fontColor = Color.white;

            up = Drawables.main_rounded_right;
            over = Drawables.darkmain_rounded_right;
            down = Drawables.active_rounded_right;
            checked = Drawables.active_rounded_right;
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
            disabled = Drawables.darkmain_rounded;
            checked = Drawables.active_rounded;

            imageDisabledColor = Color.gray;
            imageUpColor = Color.white;
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

        background = new ImageButtonStyle() {{
            up = Drawables.white_rounded;
            over = Drawables.tint(Drawables.white_rounded, Color.lightGray);
            down = Drawables.tint(Drawables.white_rounded, Color.gray);
        }};

        scene.addStyle(LabelStyle.class, label = new LabelStyle() {{
            font = Fonts.def;
        }});

        scene.addStyle(TextFieldStyle.class, field = new TextFieldStyle() {{
            font = Fonts.def;
            fontColor = Color.white;
            messageFont = Fonts.def;
            messageFontColor = Color.gray;
            disabledFontColor = Color.gray;

            background = Drawables.field_main;
            focusedBackground = Drawables.field_focused;
            invalidBackground = Drawables.field_invalid;

            cursor = Drawables.cursor;
            selection = Drawables.selection;
        }});

        scene.addStyle(ScrollPaneStyle.class, scroll = new ScrollPaneStyle() {{
            vScrollKnob = Drawables.active_knob;
            hScrollKnob = Drawables.active_knob;
        }});

        scene.addStyle(SliderStyle.class, slider = new SliderStyle() {{
            background = Drawables.empty;
            knobBefore = Drawables.active_knob;
            knobAfter = Drawables.darkmain_knob;

            knob = Drawables.slider_knob;
            knobOver = Drawables.slider_knob;
            knobDown = Drawables.active_rounded;
        }});

        scene.addStyle(DialogStyle.class, dialog = new DialogStyle() {{
            titleFont = Fonts.def;

            titleFontColor = Palette.active;
            background = Drawables.main_rounded;
            stageBackground = Drawables.gray;
        }});

        scene.addStyle(CheckBoxStyle.class, checkbox = new CheckBoxStyle() {{
            font = Fonts.def;
            fontColor = Color.white;
            checkboxOff = Drawables.switch_bg;
        }});
    }
}