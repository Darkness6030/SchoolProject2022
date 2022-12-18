package dark.ui.dialogs;

import arc.func.Floatc;
import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import dark.ui.Icons;
import dark.ui.elements.TextSlider;

import static arc.Core.bundle;
import static dark.Main.ui;

public class ColorPickerDialog extends BaseDialog {

    public Table sliders;
    public Color tmp = Color.white.cpy(), color;

    public ColorPickerDialog() {
        super("Color Picker");

        // TODO square gradient
        cont.image().size(256f).update(image -> image.setColor(tmp)).row();
        sliders = cont.table().get();

        addCloseButton();
        addButton("@ok", Icons.ok, () -> {
            ui.colorWheel.add(color.set(tmp));
            hide();
        });
    }

    public void show(Color color) {
        tmp.set(this.color = color);
        sliders.clear();

        slider(tmp.r, "color.red", tmp::r);
        slider(tmp.g, "color.green", tmp::g);
        slider(tmp.b, "color.blue", tmp::b);
        slider(tmp.a, "color.alpha", tmp::a);

        super.show();
    }

    private void slider(float def, String key, Floatc setter) {
        new TextSlider(0f, 255f, 1f, def * 255f, value -> {
            setter.get(value / 255f);
            return bundle.format(key, value.intValue());
        }).build(sliders).growX().row();
    }
}