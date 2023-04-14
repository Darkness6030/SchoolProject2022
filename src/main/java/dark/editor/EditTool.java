package dark.editor;

import arc.func.*;
import arc.graphics.*;
import arc.scene.ui.layout.*;
import arc.util.Tmp;
import dark.ui.*;
import dark.ui.elements.*;

import java.util.concurrent.atomic.AtomicInteger;

import static arc.Core.*;
import static dark.Main.*;
import static dark.editor.Renderer.background;

public enum EditTool {
    pencil(Binding.pencil) {
        {
            draggable = true;
        }

        public void build() {
            config.buildBrushTable();
        }

        public void touched(Layer current, int x, int y, Color color) {
            if (config.square)
                current.drawSquare(x, y, config.size, Tmp.c1.set(color).a(config.alpha / 255f));
            else
                current.drawCircle(x, y, config.size, Tmp.c1.set(color).a(config.alpha / 255f));
        }
    },

    eraser(Binding.eraser) {
        {
            draggable = true;
        }

        public void build() {
            config.buildBrushTable();
        }

        public void touched(Layer current, int x, int y, Color color) {
            if (config.square)
                current.drawSquare(x, y, config.size, Color.clear);
            else
                current.drawCircle(x, y, config.size, Color.clear);
        }

        public void drawOverlay(int x, int y) {
            if (config.square)
                editor.renderer.overlay.drawSquare(x, y, config.size, Palette.active);
            else
                editor.renderer.overlay.drawCircle(x, y, config.size, Palette.active);
        }
    },

    fill( Binding.fill) {
        public void build() {
            config.defaults().padRight(8f);
            config.field("@hud.alpha", config.alpha, 0, 255, 1, value -> config.alpha = value);
            config.field("@hud.tolerance", config.tolerance, 0, 100, 1, value -> config.tolerance = value);
        }

        public void touched(Layer current, int x, int y, Color color) {
            current.fill(x, y, config.tolerance / 100f, Tmp.c1.set(color).a(config.alpha / 255f));
        }

        public void drawOverlay(int x, int y) {}
    },

    line(Binding.line) {
        {
            drawOnRelease = true;
        }

        public void build() {
            config.buildBrushTable();
            config.toggle("@hud.straight", value -> config.straight = value); // всегда прямой угол
        }

        public void touched(Layer current, int x, int y, Color color) {
            if (config.square)
                current.drawSquare(x, y, config.size, Tmp.c1.set(color).a(config.alpha / 255f));
            else
                current.drawCircle(x, y, config.size, Tmp.c1.set(color).a(config.alpha / 255f));
        }
    },

    pick(Binding.pick) {
        public void build() {
            config.defaults().padRight(8f);
            config.toggle("@hud.pick-raw", value -> config.pickRaw = value);
        }

        public void touched(Layer current, int x, int y, Color color) {
            if (scene.hasMouse()) return;

            if (config.pickRaw) {
                for (int i = editor.renderer.layers.size - 1; i >= 0; i--) {
                    int raw = editor.renderer.layers.get(i).get(x, y);
                    if (raw != Color.clearRgba) {
                        ui.colorWheel.add(color.set(raw));
                        return;
                    }
                }
            } else {
                var result = new AtomicInteger(background);
                editor.renderer.layers.each(layer -> result.set(Pixmap.blend(layer.get(x, y), result.get())));

                if (result.get() == background) return;
                ui.colorWheel.add(color.set(result.get()));
            }
        }

        public void drawOverlay(int x, int y) {}
    };

    public boolean draggable;
    public boolean drawOnRelease;

    public final Binding hotkey;
    public final Config config = new Config();

    EditTool(Binding hotkey) {
        this.hotkey = hotkey;
    }

    public abstract void build();

    public abstract void touched(Layer layer, int x, int y, Color color);

    public void drawOverlay(int x, int y) {
        touched(editor.renderer.overlay, x, y, Palette.active);
    }

    public void button(Table table) {
        table.button(Icons.drawable(name()), Styles.imageButtonCheck, 48f, () -> handler.tool(this))
                .checked(button -> handler.tool == this)
                .tooltip("@" + name() + ".tooltip")
                .size(48f).pad(8f, 8f, 0f, 8f).row();
    }

    public static class Config extends Table {
        public int size = 16, alpha = 255, tolerance = 16;
        public boolean square, straight, pickRaw;

        public void buildBrushTable() {
            defaults().padRight(8f);

            field("@hud.size", size, 1, 100, 1, value -> size = value);
            field("@hud.alpha", alpha, 0, 255, 1, value -> alpha = value);

            toggle("@hud.square", value -> square = value);
        }

        public Cell<Field> field(String name, int def, int min, int max, int step, Intc listener) {
            return add(new Field(name, 64f, def, 3, min, max, listener)).with(field -> new SliderTable(field, min, max, step, listener));
        }

        public Cell<Switch> toggle(String text, Boolc listener) {
            return add(new Switch(text, listener));
        }
    }
}