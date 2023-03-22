package dark.editor;

import arc.func.*;
import arc.graphics.*;
import arc.scene.style.Drawable;
import arc.scene.ui.*;
import arc.scene.ui.TextField.TextFieldFilter;
import arc.scene.ui.layout.*;
import arc.util.Strings;
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
                current.drawSquare(x, y, config.size, color);
            else
                current.drawCircle(x, y, config.size, color);
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
            configTable.slider(0f, 1f, 0.01f, value -> config.maxDifference = value).padRight(8f);
        }

        public void touched(Layer current, int x, int y, Color color) {
            current.fill(x, y, config.maxDifference, color);
        }
    },

    line(false) {
        public void build() {
            config.buildBrushTable();
            configTable.add(new Switch("@hud.straight", value -> config.straight = value)); // всегда прямой угол
        }

        public void touched(Layer layer, int x, int y, Color color) {

        }
    },

    pick(false, Binding.pick) {
        public void build() {
            config.check("@hud.pick-raw", value -> config.pickRaw = value);
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
    public final Table configTable = new Table();

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
                    editor.tool = this;
                    ui.hudFragment.updateConfig();
                })
                .checked(button -> editor.tool == this)
                .tooltip("@" + name() + ".tooltip")
                .size(48f).pad(8f, 8f, 0f, 8f).row();
    }

    public class Config {
        public int size = 16, softness = 20;
        public float maxDifference = 0.2f;

        public boolean square, straight, pickRaw;

        public void buildBrushTable() {
            image(Icons.circle).size(24f);
            field(size, 1, 100, 1, value -> size = value).padRight(8f);

            image(Icons.spray).size(24f);
            field(softness, 1, 100, 1, value -> softness = value);

            check("@hud.square", value -> square = value).pad(0f, 8f, 0f, 8f);
        }

        public Cell<Image> image(Drawable drawable) {
            return configTable.image(drawable);
        }

        public Cell<TextField> field(int def, int min, int max, int step, Intc listener) {
            return configTable.field(String.valueOf(def), TextFieldFilter.digitsOnly, value -> listener.get(Strings.parseInt(value)))
                    .with(field -> new SliderTable(field, min, max, step))
                    .valid(value -> {
                        int number = Strings.parseInt(value);
                        return number >= min && number <= max;
                    });
        }

        public Cell<Switch> check(String text, Boolc listener) {
            return configTable.add(new Switch(text, listener));
        }
    }
}