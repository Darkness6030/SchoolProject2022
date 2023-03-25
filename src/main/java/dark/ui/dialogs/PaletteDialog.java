package dark.ui.dialogs;

import arc.func.Cons;
import arc.func.Floatc;
import arc.func.Floatp;
import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.Image;
import arc.scene.ui.TextField;
import arc.scene.ui.TextField.TextFieldFilter;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Strings;
import dark.ui.Drawables;
import dark.ui.Icons;
import dark.ui.Palette;

import static arc.Core.app;
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

        buttons.buttonRow("@ok", Icons.ok, () -> {
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
            ctrl.image(Drawables.white_rounded_right).update(image -> image.setColor(model.get()));

            ctrl.row();

            field(ctrl, "[red]R: ", 255, model::red, model::red);
            field(ctrl, "[gray]H: ", 360, model::hue, model::hue);

            ctrl.row();

            field(ctrl, "[green]G: ", 255, model::green, model::green);
            field(ctrl, "[gray]S: ", 100, model::saturation, model::saturation);

            ctrl.row();

            field(ctrl, "[blue]B: ", 255, model::blue, model::blue);
            field(ctrl, "[gray]V: ", 100, model::value, model::value);

            ctrl.row();

            field(ctrl, "[accent]HEX: ", model::hex, model::hex, field -> {
                field.setFilter((f, c) -> validHexChars.contains(String.valueOf(c)));
                field.setValidator(hex -> hex.length() == 6 || hex.length() == 8);
                field.setMaxLength(8);

                app.post(() -> ((Table) field.parent).getCells().peek().width(182f)); // увеличивает размер поля под hex
            });
        }).growY();
    }

    public void field(Table table, String name, Cons<String> cons, Prov<String> prov, Cons<TextField> with) {
        table.table(cont -> {
            cont.left();

            cont.add(name);
            cont.field("", TextFieldFilter.digitsOnly, text -> {
                cons.get(text);
                rebuild();
            }).with(field -> {
                with.get(field);
                rebuild.add(() -> {
                    if (!field.hasKeyboard()) field.setText(prov.get());
                });
            }).width(80f);
        }).marginLeft(6f);
    }

    public void field(Table table, String name, int max, Floatc cons, Floatp prov) {
        field(table, name, t -> cons.get(Strings.parseInt(t)), () -> String.valueOf((int) prov.get()), field -> {
            field.setMaxLength(3);
            field.setValidator(text -> {
                int number = Strings.parseInt(text);
                return number >= 0 && number <= max;
            });
        });
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

            pixmap.each((x, y) -> pixmap.set(x, y, Color.HSVtoRGB(360f - y, model.saturation, model.value)));
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
            var hsv = Color.RGBtoHSV(color);
            set(hsv[0], hsv[1], hsv[2]);
        }

        public Color get() {
            return Color.HSVtoRGB(hue, saturation, value);
        }

        public Color get(Color color) {
            return Color.HSVtoRGB(hue, saturation, value, color);
        }

        // region rgb

        public float red() {
            return (int) (Color.HSVtoRGB(hue, saturation, value).r * 255f);
        }

        public float green() {
            return (int) (Color.HSVtoRGB(hue, saturation, value).g * 255f);
        }

        public float blue() {
            return (int) (Color.HSVtoRGB(hue, saturation, value).b * 255f);
        }

        public void red(float red) {
            set(Color.HSVtoRGB(hue, saturation, value).r(red / 255f));
        }

        public void green(float green) {
            set(Color.HSVtoRGB(hue, saturation, value).g(green / 255f));
        }

        public void blue(float blue) {
            set(Color.HSVtoRGB(hue, saturation, value).b(blue / 255f));
        }

        // endregion
        // region hsv

        public float hue() {
            return hue;
        }

        public float saturation() {
            return saturation;
        }

        public float value() {
            return value;
        }

        public void hue(float hue) {
            this.hue = hue;
        }

        public void saturation(float saturation) {
            this.saturation = saturation;
        }

        public void value(float value) {
            this.value = value;
        }

        // endregion
        // region hex

        public String hex() {
            return get().toString();
        }

        public void hex(String hex) {
            set(Color.valueOf(hex));
        }

        // endregion
    }
}