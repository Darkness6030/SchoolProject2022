package dark.ui.elements;

import arc.scene.ui.TextField;
import arc.util.Strings;

public class SliderTable extends UnderTable {

    public SliderTable(TextField field, int min, int max, int step) {
        super(field, cont -> {
            cont.slider(min, max, step, value -> field.setText(String.valueOf((int) value))).update(slider -> {
                if (!slider.isDragging() && Strings.canParseInt(field.getText())) slider.setValue(Strings.parseInt(field.getText()));
            });
        });

        field.clicked(this::show);
        update(() -> {
            if (!field.hasKeyboard()) root.clear();
        });
    }
}
