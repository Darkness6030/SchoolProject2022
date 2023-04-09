package dark.ui.dialogs;

import arc.func.*;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import dark.ui.Drawables;
import dark.ui.Palette;
import dark.ui.elements.Field;

import static arc.Core.input;
import static dark.Main.ui;

public class PaletteDialog extends BaseDialog {

    public static final String validHexChars = "0123456789AaBbCcDdEeFf";

    public PaletteImage image = new PaletteImage();
    public HueSlider slider = new HueSlider();

    public Color callback;
    public ColorModel model = new ColorModel();

    public Seq<Runnable> rebuild = Seq.with(image::update, slider::update);

    public PaletteDialog() {
        super("@palette");

        addCloseButton();
        addConfirmButton(() -> {
            ui.colorWheel.add(model.get(callback));
            hide();
        });

        cont.marginTop(12f);
        cont.add(image).size(256f).padRight(16f);
        cont.add(slider).size(32f, 256f).padRight(16f);

        cont.table(ctrl -> {
            ctrl.top();
            ctrl.defaults().size(128f, 32f);

            ctrl.image(Drawables.white_rounded_left).update(image -> image.setColor(callback));
            ctrl.image(Drawables.white_rounded_right).update(image -> image.setColor(model.get(image.color)));

            ctrl.row();

            field(ctrl, "[red]R:", 255, model::red, model::red);
            field(ctrl, "[gray]H:", 360, model::hue, model::hue);

            ctrl.row();

            field(ctrl, "[green]G:", 255, model::green, model::green);
            field(ctrl, "[gray]S:", 100, model::saturation, model::saturation);

            ctrl.row();

            field(ctrl, "[blue]B:", 255, model::blue, model::blue);
            field(ctrl, "[gray]V:", 100, model::value, model::value);

            ctrl.row();

            field(ctrl, "[accent]HEX:", model::hex, model::hex);
        }).growY();
    }

    public void field(Table table, String name, Cons<String> cons, Prov<String> prov) {
        table.add(new Field(name, 181f, "", text -> {
            cons.get(text);
            rebuild();
        })).with(field -> {
            field.filter((f, c) -> validHexChars.contains(String.valueOf(c)));
            field.valid(hex -> hex.length() == 6);
            field.maxTextLength(6);

            rebuild.add(() -> field.setTextSafe(prov.get()));
        });
    }

    public void field(Table table, String name, int max, Floatc cons, Floatp prov) {
        table.add(new Field(name, 80f, 0, 3, 0, max, (int value) -> {
            cons.get(value);
            rebuild();
        })).with(field -> rebuild.add(() -> field.setTextSafe(String.valueOf((int) prov.get()))));
    }

    public void rebuild() {
        rebuild.each(Runnable::run);
    }

    public void show(Color color) {
        this.callback = color;
        this.model.set(color);

        rebuild();
        super.show();
    }

    public static abstract class ClickableImage extends Image {
        public Pixmap pixmap;
        public Texture texture;

        public Vec2 mouse = new Vec2();
        public boolean clicked;

        public ClickableImage(int width, int height) {
            pixmap = new Pixmap(width, height);
            texture = new Texture(pixmap);
            setDrawable(new TextureRegion(texture));

            tapped(() -> clicked = true);
            released(() -> clicked = false);
        }

        /** Подтягивает изменения цвета, внесённые другими элементами. */
        public abstract void update();
    }

    public class PaletteImage extends ClickableImage {

        public PaletteImage() {
            super(100, 100);
            update(() -> {
                if (!clicked) return;

                mouse.set(screenToLocalCoordinates(input.mouse()).clamp(0f, 0f, 256f, 256f));

                model.saturation(mouse.x / 2.56f);
                model.value(mouse.y / 2.56f);

                rebuild();
            });
        }

        @Override
        public void update() {
            if (!clicked) mouse.set(model.saturation * 2.56f, model.value * 2.56f);

            pixmap.each((x, y) -> pixmap.set(x, y, Color.HSVtoRGB(model.hue, x, 100f - y)));
            texture.load(texture.getTextureData());
        }

        @Override
        public void draw() {
            super.draw();

            Lines.stroke(3f, Palette.active.cpy().a(parentAlpha));
            Lines.poly(x + mouse.x, y + mouse.y, 16, 9f);
        }
    }

    public class HueSlider extends ClickableImage {

        public HueSlider() {
            super(1, 360);
            update(() -> {
                if (!clicked) return;

                mouse.set(screenToLocalCoordinates(input.mouse()).clamp(0f, 0f, 256f, 32f));
                model.hue(mouse.y * 1.40625f);

                rebuild();
            });
        }

        @Override
        public void update() {
            if (!clicked) mouse.set(0f, model.hue / 1.40625f);

            pixmap.each((x, y) -> pixmap.set(x, y, Color.HSVtoRGB(360f - y, 100f, 100f)));
            texture.load(texture.getTextureData());
        }

        @Override
        public void draw() {
            super.draw();

            Lines.stroke(3f, Palette.active.cpy().a(parentAlpha));
            Lines.rect(x - 3f, y + Mathf.clamp(mouse.y - 6f, 0f, height - 12f), width + 6f, 12f);
        }
    }

    public static class ColorModel {
        public float hue, saturation, value;

        public void set(float hue, float saturation, float value) {
            this.hue = hue;
            this.saturation = saturation;
            this.value = value;
        }

        public void set(Color color) {
            set(color.hue(), color.saturation() * 100f, color.value() * 100f);
        }

        public Color get(Color color) {
            return color.fromHsv(hue, saturation / 100f, value / 100f);
        }

        // region rgb

        public void red(float red) {
            set(Color.HSVtoRGB(hue, saturation, value).r(red / 255f));
        }

        public void green(float green) {
            set(Color.HSVtoRGB(hue, saturation, value).g(green / 255f));
        }

        public void blue(float blue) {
            set(Color.HSVtoRGB(hue, saturation, value).b(blue / 255f));
        }

        public float red() {
            return Color.HSVtoRGB(hue, saturation, value).r * 255f;
        }

        public float green() {
            return Color.HSVtoRGB(hue, saturation, value).g * 255f;
        }

        public float blue() {
            return Color.HSVtoRGB(hue, saturation, value).b * 255f;
        }

        // endregion
        // region hsv

        public void hue(float hue) {
            this.hue = hue;
        }

        public void saturation(float saturation) {
            this.saturation = saturation;
        }

        public void value(float value) {
            this.value = value;
        }

        public float hue() {
            return hue;
        }

        public float saturation() {
            return saturation;
        }

        public float value() {
            return value;
        }

        // endregion
        // region hex

        public void hex(String hex) {
            set(Color.valueOf(hex));
        }

        public String hex() {
            return String.format("%02x%02x%02x", (int) red(), (int) green(), (int) blue());
        }

        // endregion
    }
}