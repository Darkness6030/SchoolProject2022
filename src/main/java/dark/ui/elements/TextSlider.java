package dark.ui.elements;

import arc.func.Cons;
import arc.func.Func;
import arc.graphics.Color;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.scene.utils.Disableable;

public class TextSlider extends Table implements Disableable {

    public Label label;
    public Slider slider;

    public TextSlider(float min, float max, float step, float def, Func<Float, String> text) {
        touchable = Touchable.disabled;

        label = labelWrap("").padLeft(12f).growX().left().get();
        slider = new Slider(min, max, step, false);

        slider.moved(value -> label.setText(text.get(value)));
        slider.setValue(def);
        slider.change();
    }

    public Cell<Stack> build(Table table) {
        return table.stack(slider, this);
    }

    public TextSlider update(Cons<TextSlider> cons) {
        update(() -> cons.get(this));
        return this;
    }

    @Override
    public boolean isDisabled() {
        return slider.isDisabled();
    }

    @Override
    public void setDisabled(boolean isDisabled) {
        slider.setDisabled(isDisabled);
        Color color = isDisabled ? Color.gray : Color.white;
        slider.setColor(color);
        label.setColor(color);
    }
}