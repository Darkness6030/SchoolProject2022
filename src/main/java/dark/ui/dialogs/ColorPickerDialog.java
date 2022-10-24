package dark.ui.dialogs;

import arc.func.Floatc;
import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import dark.ui.Icons;
import dark.ui.elements.TextSlider;

public class ColorPickerDialog extends BaseDialog {

    public Table sliders;
    public Color tmp = Color.white.cpy(), callback;

    public ColorPickerDialog() {
        super("Color Picker");

        // TODO square gradient

        cont.image().size(256f).update(image -> image.setColor(tmp)).row();
        sliders = cont.table().get();

        addCloseButton();
        addButton(Icons.ok, "Ok", () -> {
            this.hide();
            callback.set(tmp);
        });
    }

    public void show(Color color) {
        tmp.set(callback = color);
        sliders.clear();

        slider(tmp.r, 'R', tmp::r);
        slider(tmp.g, 'G', tmp::g);
        slider(tmp.b, 'B', tmp::b);
        slider(tmp.a, 'A', tmp::a);

        super.show();
    }

    private void slider(float def, char component, Floatc setter) {
        new TextSlider(0f, 255f, 1f, def * 255f, value -> {
            setter.get(value / 255f);
            return component + ": " + value.intValue();
        }).build(sliders).growX().row();
    }
}
