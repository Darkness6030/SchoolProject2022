package dark.ui.fragments;

import arc.graphics.Color;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.*;
import dark.editor.EditTool;
import dark.editor.Layer;
import dark.editor.Renderer;
import dark.ui.Drawables;
import dark.ui.Icons;
import dark.ui.Styles;
import dark.ui.elements.FocusScrollPane;

import static arc.Core.*;
import static dark.Main.*;

public class HudFragment {

    public Table layers;
    public FocusScrollPane pane;

    public void build(WidgetGroup parent) {
        parent.fill(cont -> { // tools parameters
            cont.name = "Tools parameters";
            cont.top();

            cont.table(Drawables.main, pad -> {
                pad.left();
                pad.button(Drawables.alpha_chan, Styles.alpha, 40f, () -> ui.menu.show()).checked(b -> ui.menu.isShown()).size(48f).pad(8f);

                pad.slider(1f, 100f, 1f, value -> editor.brushSize = (int) value).padLeft(48f);
                pad.check("@hud.square", value -> editor.square = value).padLeft(48f);
            }).height(64f).growX();
        });

        parent.fill(cont -> { // tools
            cont.name = "Tools";
            cont.left();

            cont.table(Drawables.main, pad -> {
                pad.top();
                for (var type : EditTool.values()) type.button(pad);

                pad.stack(
                        new SwapButton(editor.first, editor.second, 18f, 18f),
                        new ColorBlob(editor.second, 8f, -8f),
                        new ColorBlob(editor.first, -8f, 8f)
                ).size(32f).padTop(24f);
            }).width(64f).growY().padTop(64f);
        });

        parent.fill(cont -> { // layers
            cont.name = "Layers";
            cont.right();

            cont.table(Drawables.main, pad -> {
                pane = new FocusScrollPane(layers = new Table().top());

                pane.setScrollingDisabledX(true);
                pane.setOverscroll(true, true);
                pane.setFadeScrollBars(true);

                pad.add(pane).grow().row();
                updateLayers();

                pad.table(act -> {
                    // кнопки движения
                    act.defaults().size(32f).pad(8f, 8f, 8f, 0f);

                    act.button(Icons.up, () -> {})
                            .disabled(b -> true)
                            .tooltip("@layer.move.up");

                    act.button(Icons.down, () -> {})
                            .disabled(b -> true)
                            .tooltip("@layer.move.down");

                    // пустое пространство между кнопками
                    act.add().fillX();

                    // кнопки создания и удаления
                    act.defaults().size(32f).pad(8f, 0f, 8f, 8f);

                    act.button(Icons.plus, editor::newLayer)
                            .disabled(b -> !editor.renderer.canAdd())
                            .tooltip(bundle.format("layer.new.tooltip", Renderer.maxLayers));

                    act.button(Icons.copy, () -> {})
                            .disabled(b -> !editor.renderer.canAdd())
                            .tooltip("@layer.copy.tooltip");

                    act.button(Icons.trash, () -> {})
                            .disabled(b -> !editor.renderer.canRemove())
                            .tooltip("@layer.remove.tooltip");
                }).height(48f).growX();
            }).width(256f).growY().padTop(64f);
        });

        parent.fill(cont -> { // corners
            cont.name = "Corners";
            cont.top();

            cont.image(Drawables.corners).height(7f).growX().pad(64f, 64f, 0f, 256f);
        });
    }

    public void updateLayers() {
        if (layers == null) return; // такое возможно?

        layers.clear(); // Цикл нужен для проходки в обратном порядке, т.к. в конце массива расположены верхние слои
        for (int i = editor.renderer.layers.size - 1; i >= 0; i--)
            layers.add(new LayerButton(editor.renderer.layers.get(i))).height(64f).growX().padTop(8f).row();
    }

    // region subclasses

    public static class SwapButton extends ImageButton {

        public SwapButton(Color first, Color second, float x, float y) {
            super(Icons.swap, Styles.imageNoneStyle);
            setTranslation(x, y);

            clicked(() -> {
                Tmp.c1.set(first);
                first.set(second.cpy());
                second.set(Tmp.c1);

                ui.showInfoToast(Icons.swap, "@swapped");
            });
        }
    }

    public static class ColorBlob extends ImageButton {

        public ColorBlob(Color color, float x, float y) {
            super(Drawables.color_blob, Styles.imageNoneStyle);
            setTranslation(x, y);

            clicked(() -> ui.palette.show(color));
            update(() -> getImage().setColor(color));
        }
    }

    public static class LayerButton extends ImageButton {

        public Layer layer;

        public LayerButton(Layer layer) {
            super(layer.region, Styles.layer);
            this.layer = layer;

            getImageCell().size(64f).padLeft(8f);
            defaults().padLeft(8f);

            field(layer.name, text -> layer.name = text).fillX();
            button(Icons.eyeOpen, Styles.visible, 12f, () -> layer.visible = !layer.visible).checked(button -> !layer.visible).size(32f).padRight(8f).row();

            clicked(() -> {
                editor.renderer.current = layer;
                ui.hudFragment.pane.scrollToY(this.y - 264f);
            });

            update(() -> setChecked(editor.renderer.current == layer));
        }
    }

    // endregion
}