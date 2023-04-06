package dark.editor;

import arc.func.*;
import arc.graphics.*;
import arc.scene.ui.layout.*;
import dark.ui.*;
import dark.ui.elements.*;

import java.util.concurrent.atomic.AtomicInteger;

import static arc.Core.scene;
import static dark.Main.*;
import static dark.editor.Renderer.background;

public enum EditTool {
    pencil(true, Binding.pencil) {
        public void build() {
            config.buildBrushTable();
        }

        public void touched(Layer current, int x, int y, Color color) {
            if (config.square)
                current.drawSquare(x, y, config.size, color.cpy().a(config.alpha / 255f));
            else
                current.drawCircle(x, y, config.size, color.cpy().a(config.alpha / 255f));
        }
    },

    eraser(true, Binding.eraser) {
        public void build() {
            config.buildBrushTable();
        }

        public void touched(Layer current, int x, int y, Color color) {
            if (config.square)
                current.drawSquare(x, y, config.size, Color.clear);
            else
                current.drawCircle(x, y, config.size, Color.clear);
        }
    },

    fill(false, Binding.fill) {
        public void build() {
            config.field("@hud.tolerance", config.tolerance, 0, 100, 1, value -> config.tolerance = value);
        }

        public void touched(Layer current, int x, int y, Color color) {
            current.fill(x, y, config.tolerance / 100f, color.cpy().a(config.alpha / 255f));
        }
    },

    line(false) {
        public void build() {
            config.buildBrushTable();
            config.toggle("@hud.straight", value -> config.straight = value); // всегда прямой угол
        }

        public void touched(Layer layer, int x, int y, Color color) {

        }
    },

    pick(false, Binding.pick) {
        public void build() {
            config.toggle("@hud.pick-raw", value -> config.pickRaw = value);
        }

        public void touched(Layer current, int x, int y, Color color) {
            if (scene.hasMouse()) return;

            var result = new AtomicInteger(background);
            editor.renderer.layers.each(layer -> result.set(Pixmap.blend(layer.get(x, y), result.get())));

            if (result.get() == background) return;
            ui.colorWheel.add(color.set(result.get()));
        }
    };

    public final boolean draggable;
    public final Binding hotkey;

    public final Config config = new Config();

    EditTool(boolean draggable) {
        this(draggable, Binding.unknown);
    }

    EditTool(boolean draggable, Binding hotkey) {
        this.draggable = draggable;
        this.hotkey = hotkey;
    }

    public abstract void build();

    public abstract void touched(Layer layer, int x, int y, Color color);

    public void button(Table table) {
        table.button(Icons.drawable(name()), Styles.imageButtonCheck, 48f, () -> {
                    handler.tool(this);
                    ui.hudFragment.updateConfig();
                })
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
            return add(new Field(name, 64f, def, 3, min, max, listener)).with(field -> {
                new SliderTable(field, min, max, step, listener);
            });
        }

        public Cell<Switch> toggle(String text, Boolc listener) {
            return add(new Switch(text, listener));
        }
    }
}