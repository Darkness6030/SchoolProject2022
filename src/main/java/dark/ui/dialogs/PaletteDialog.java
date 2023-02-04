package dark.ui.dialogs;

import arc.func.Floatc;
import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import dark.ui.Icons;

import static dark.Main.ui;

public class PaletteDialog extends BaseDialog {

    public Table sliders;
    public Color tmp = Color.white.cpy(), color;

    public PaletteDialog() {
        super("@color.pick");
        closeOnBack();

        // TODO square gradient
        cont.image().size(256f).update(image -> image.setColor(tmp)).row();
        sliders = cont.table().get();

        buttons.buttonRow("@close", Icons.cancel, this::hide);
        buttons.buttonRow("@ok", Icons.ok, () -> {
            hide();

            ui.colorWheel.add(color.set(tmp));
        });
    }

    public void show(Color color) {
        tmp.set(this.color = color);
        sliders.clear();

        slider("color.red", tmp.r, tmp::r);
        slider("color.green", tmp.g, tmp::g);
        slider("color.blue", tmp.b, tmp::b);
        slider("color.alpha", tmp.a, tmp::a);

        super.show();
    }

    private void slider(String title, float def, Floatc setter) {
        //cont.add(new TextFieldSlider(title, 1f, 255f, 1f, def * 255f, value -> setter.get(value / 255f))).growX().row();
    }
}