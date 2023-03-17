package dark.ui.dialogs;

import arc.func.Cons;
import arc.func.Func;
import arc.graphics.Color;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;

import static arc.Core.*;

// TODO оно вообще нужно?
public class SettingsDialog extends BaseDialog {

    public SettingsDialog() {
        super("@settings");
        addCloseButton();

        var volume = new SliderSetting("sfxvol", 1, 100, 1, 100, value -> value + "%");
        volume.build(cont);
    }

    public abstract static class Setting {
        public final String name, title, description;

        public Setting(String name) {
            this.name = name;
            this.title = bundle.getOrNull("setting." + name + ".title");
            this.description = bundle.getOrNull("setting." + name + ".description");
        }

        public abstract void build(Table table);
    }

    public static class SliderSetting extends Setting {

        public final int min, max, step, def;
        public final Func<Integer, String> func;
        public final Cons<Integer> cons;

        public SliderSetting(String name, int min, int max, int step, int def, Func<Integer, String> func) {
            this(name, min, max, step, def, func, value -> {});
        }

        public SliderSetting(String name, int min, int max, int step, int def, Func<Integer, String> func, Cons<Integer> cons) {
            super(name);

            this.min = min;
            this.max = max;
            this.step = step;
            this.def = def;

            this.func = func;
            this.cons = cons;
        }

        @Override
        public void build(Table table) {
            var label = new Label("");
            label.setColor(Color.white);

            var content = new Table();
            content.touchable = Touchable.disabled;

            content.add(title).left().growX().wrap();
            content.add(label).right().padLeft(10f);
            content.margin(3f, 33f, 3f, 33f);

            var slider = new Slider(min, max, step, false);
            slider.setValue(def);

            slider.changed(() -> {
                settings.put(name, (int) slider.getValue());
                label.setText(func.get((int) slider.getValue()));
            });

            slider.change();

            table.stack(slider, content).width(800f).left().padTop(4f).tooltip(description);
        }
    }
}