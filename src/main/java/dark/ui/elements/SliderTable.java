package dark.ui.elements;

import arc.func.Intc;
import arc.util.Strings;

public class SliderTable extends UnderTable {

    public SliderTable(Field field, int min, int max, int step, Intc listener) {
        super(field, cont -> cont.slider(min, max, step, value -> { // Дарк, не пиши всё в одну строчку - это прочитать сложно
            listener.get((int) value);

            field.setText(String.valueOf((int) value));
            field.setCursorPosition(field.getText().length());
        }).update(slider -> {
            if (!slider.isDragging() && Strings.canParseInt(field.getText())) slider.setValue(Strings.parseInt(field.getText()));
        }));

        field.onClicked(this::show);
        update(() -> {
            if (!field.focused()) root.clear();
        });
    }
}