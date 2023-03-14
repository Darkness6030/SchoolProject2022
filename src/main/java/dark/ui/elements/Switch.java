package dark.ui.elements;

import arc.func.Boolc;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.scene.ui.CheckBox;
import dark.ui.Palette;

public class Switch extends CheckBox {

    public final Color checked, unchecked;
    public float knobX = 0f;

    public Switch(String text, Boolc listener) {
        this(text, Palette.active, Palette.darkmain, listener);
    }

    public Switch(String text, Color checked, Color unchecked, Boolc listener) {
        super(text);
        this.checked = checked;
        this.unchecked = unchecked;

        this.changed(() -> listener.get(isChecked()));
        this.getImage().color.set(Palette.darkmain);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.knobX = Mathf.lerpDelta(knobX, isChecked() ? 20f : 0f, .1f);
        this.getImage().color.lerp(isChecked() ? checked : unchecked, .1f);
    }

    @Override
    public void draw() {
        super.draw();

        Draw.color(Palette.main);
        Draw.rect("whiteui-rounded", knobX + x + 12f, y + 12f, 16f, 16f);
    }
}