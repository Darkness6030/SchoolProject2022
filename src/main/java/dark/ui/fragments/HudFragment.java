package dark.ui.fragments;

import arc.graphics.Color;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import dark.editor.EditTool;
import dark.editor.Layer;
import dark.editor.Renderer;
import dark.history.Operation;
import dark.ui.Drawables;
import dark.ui.Icons;
import dark.ui.Styles;
import dark.ui.elements.FocusScrollPane;

import static arc.Core.*;
import static dark.Main.*;

public class HudFragment {

    public Table config, layers;
    public FocusScrollPane pane;
    public boolean historyTab;

    public void build(WidgetGroup parent) {
        parent.fill(cont -> { // tools config
            cont.name = "Tools config";
            cont.top();

            cont.table(Drawables.main, pad -> {
                pad.left();
                pad.button(Drawables.alpha_chan, Styles.alpha, 40f, () -> ui.menu.show()).checked(b -> ui.menu.isShown()).size(48f).pad(8f);

                for (EditTool tool : EditTool.values()) tool.build();

                config = pad.table().grow().get();
                updateConfig();
            }).height(64f).growX();
        });

        parent.fill(cont -> { // tools
            cont.name = "Tools";
            cont.left();

            cont.table(Drawables.main, pad -> {
                pad.top();
                for (var type : EditTool.values()) type.button(pad);

                pad.stack(
                        new SwapButton(18f, 18f),
                        new ColorBlob(editor.second, 8f, -8f),
                        new ColorBlob(editor.first, -8f, 8f)
                ).size(32f).padTop(24f);
            }).width(64f).growY().padTop(64f);
        });

        parent.fill(cont -> { // layers & history
            cont.name = "Layers & history";
            cont.right();

            cont.table(Drawables.main, pad -> {
                pad.table(tabs -> {
                    tabs.defaults().grow();

                    tabs.button("@hud.layers", Styles.layersTab, this::buildLayers).checked(t -> !historyTab);
                    tabs.button("@hud.history", Styles.historyTab, this::buildHistory).checked(t -> historyTab);
                }).height(32f).growX().pad(0f, 8f, 10f, 8f).row();

                pane = new FocusScrollPane(layers = new Table().top());

                pane.setScrollingDisabledX(true);
                pane.setOverscroll(true, true);
                pane.setFadeScrollBars(true);

                pad.add(pane).grow().row();
                updateLayers();

                pad.table(act -> {
                    act.visible(() -> !historyTab); // во вкладке истории этих кнопок быть не должно
                    act.defaults().size(32f).pad(8f, 8f, 8f, 0f); // кнопки движения

                    act.button(Icons.up, editor::moveUp)
                            .disabled(b -> !editor.canMoveUp())
                            .tooltip("@layer.move.up");

                    act.button(Icons.down, editor::moveDown)
                            .disabled(b -> !editor.canMoveDown())
                            .tooltip("@layer.move.down");

                    act.add().fillX(); // пустое пространство между кнопками
                    act.defaults().size(32f).pad(8f, 0f, 8f, 8f); // кнопки создания и удаления

                    act.button(Icons.plus, editor::newLayer)
                            .disabled(b -> !editor.renderer.canAdd())
                            .tooltip(bundle.format("layer.new", Renderer.maxLayers));

                    act.button(Icons.copy, editor::copyLayer)
                            .disabled(b -> !editor.renderer.canAdd())
                            .tooltip("@layer.copy");

                    act.button(Icons.trash, editor::removeLayer)
                            .disabled(b -> !editor.renderer.canRemove())
                            .tooltip("@layer.remove");
                }).height(48f).growX();
            }).width(256f).growY().padTop(64f);
        });

        parent.fill(cont -> { // corners
            cont.name = "Corners";
            cont.top();

            cont.image(Drawables.corners).height(7f).growX().pad(64f, 64f, 0f, 256f);
        });
    }

    public void updateConfig() {
        config.clear();
        config.left().add(editor.tool.config);
    }

    public void updateLayers() {
        if (!historyTab) buildLayers();
    }

    public void updateHistory() {
        if (historyTab) buildHistory();
    }

    public void buildLayers() {
        if (layers == null) return; // такое возможно?
        historyTab = false;

        layers.clear(); // цикл нужен для проходки в обратном порядке, т.к. в конце массива расположены верхние слои
        for (int i = editor.renderer.layers.size - 1; i >= 0; i--)
            layers.add(new LayerButton(editor.renderer.layers.get(i))).height(64f).growX().marginRight(8f).row();
    }

    public void buildHistory() {
        if (layers == null) return;
        historyTab = true;

        layers.clear();
        history.each(operation -> {
            layers.add(new OperationButton(operation)).height(64f).growX().margin(8f).row();
        });
    }

    // region subclasses

    public static class SwapButton extends ImageButton {

        public SwapButton(float x, float y) {
            super(Icons.swap, Styles.emptyImageButton);
            setTranslation(x, y);

            clicked(editor::swap);
        }
    }

    public static class ColorBlob extends ImageButton {

        public ColorBlob(Color color, float x, float y) {
            super(Drawables.color_blob, Styles.emptyImageButton);
            setTranslation(x, y);

            clicked(() -> ui.palette.show(color));
            update(() -> getImage().setColor(color));
        }
    }

    public static class LayerButton extends ImageButton {

        public LayerButton(Layer layer) {
            super(layer.region, Styles.layer);

            getImageCell().size(64f);
            defaults().padLeft(8f);

            field(layer.name, text -> layer.name = text).maxTextLength(12).width(120f);
            button(Icons.eyeOpen, Styles.visible, () -> layer.visible = !layer.visible).checked(button -> !layer.visible).size(32f).row();

            clicked(() -> {
                editor.renderer.current = layer;
                ui.hudFragment.pane.scrollTo(0f, this.y - 446f);
            });

            update(() -> setChecked(editor.renderer.current == layer));
        }
    }

    public static class OperationButton extends ImageButton {

        public OperationButton(Operation operation) {
            super(Drawables.alpha_chan, Styles.layer);

            defaults().padLeft(8f);

            label(() -> operation.data.size + " ints").growX();
            label(() -> operation.data.size * 4 / 1024 + " Kb").growX();

            clicked(() -> {
                ui.hudFragment.pane.scrollTo(0f, this.y - 446f);
            });

            update(() -> setChecked(history.selected() == operation));
        }
    }

    // endregion
}