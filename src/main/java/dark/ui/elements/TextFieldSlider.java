package dark.ui.elements;

import arc.func.Floatc;
import arc.scene.ui.*;
import arc.scene.ui.TextField.TextFieldFilter;
import arc.scene.ui.layout.Table;
import arc.util.Strings;

public class TextFieldSlider extends Table {

    public Label label;
    public Slider slider;
    public TextField field;

    public TextFieldSlider(String title, float min, float max, float step, float def, Floatc listener) {
        label = new Label(title);
        label.setWrap(true);

        slider = new Slider(min, max, step, false);
        slider.setValue(def);

        slider.moved(value -> {
            field.setText(String.valueOf(value));
            listener.get(value);
        });

        field = new TextField();
        field.setText(String.valueOf(def));

        field.setFilter(TextFieldFilter.floatsOnly);
        field.setValidator(text -> {
            float value = Strings.parseFloat(text);
            return value >= min && value <= max;
        });

        field.typed(character -> {
            if (!field.isValid()) return;

            var value = Strings.parseFloat(field.getText());
            slider.setValue(value);
            listener.get(value);
        });

        stack(label, slider).row();
        add(field);
    }
}