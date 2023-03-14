package dark.ui.elements;

import arc.func.Boolc;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.scene.ui.CheckBox;
import dark.ui.Palette;

public class Switch extends CheckBox {

    public float knobX = 0f;

    public Switch(String text, Boolc listener) {
        super(text);
        changed(() -> listener.get(isChecked()));

        getImage().color.set(Palette.darkmain);
    }

    public Switch(String text, boolean enabled, Boolc listener) {
        this(text, listener);
        setChecked(enabled);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        knobX = Mathf.lerpDelta(knobX, isChecked() ? 20f : 0f, .1f);
        getImage().color.lerp(isChecked() ? Palette.active : Palette.darkmain, .1f);
    }

    @Override
    public void draw() {
        super.draw();

        Draw.color(isOver() ? Palette.darkmain : Palette.main);
        Draw.rect("whiteui-rounded", knobX + x + 12f, y + 12f, 16f, 16f);
    }
}
