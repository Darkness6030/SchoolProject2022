package dark.ui.fragments;

import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import dark.editor.EditTool;
import dark.editor.Layer;
import dark.ui.*;
import dark.ui.elements.FocusScrollPane;

import static arc.Core.*;
import static dark.Main.*;
import static dark.editor.Renderer.maxLayers;

public class HudFragment {

    public Table table;
    public FocusScrollPane pane;

    public void build(WidgetGroup parent) {
        parent.fill(cont -> {
            cont.top();

            cont.table(Drawables.underline, underline -> {
                underline.left();
                underline.button(Drawables.alpha_chan, Styles.alphaStyle, 40f, () -> ui.menuDialog.show()).checked(button -> ui.menuDialog.isShown()).size(40f).padLeft(8f);

                underline.slider(1f, 100f, 1f, value -> editor.brushSize = (int) value).padLeft(48f);

                underline.stack(
                        new SwapButton(editor.first, editor.second, 18f, 18f),
                        new ColorBlob(editor.second, 8f, -8f),
                        new ColorBlob(editor.first, -8f, 8f)
                ).size(32f).padLeft(48f);

                underline.check("@hud.square", value -> editor.square = value).padLeft(48f);
            }).height(64f).growX();
        });

        parent.fill(cont -> {
            cont.left();

            cont.table(Drawables.sideline, sideline -> {
                sideline.top();
                for (var type : EditTool.values()) type.button(sideline);
            }).width(68f).growY().padTop(60f);
        });

        parent.fill(cont -> {
            cont.right();

            cont.table(Drawables.sideline_left, sideline -> {
                sideline.top();
                sideline.button("@layer.new", editor::newLayer)
                        .disabled(button -> !editor.renderer.canAdd())
                        .tooltip(bundle.format("layer.new.tooltip", maxLayers))
                        .width(128f).padTop(8f).padBottom(8f).row();

                pane = new FocusScrollPane(table = new Table());
                pane.setScrollingDisabledX(true);

                pane.setOverscroll(true, true);
                pane.setFadeScrollBars(true);

                sideline.add(pane).height(528f);

                updateLayers();
            }).width(196f).growY().padTop(60f);
        });
    }

    public void updateLayers() {
        if (table == null) return;

        table.clear();
        table.top();

        editor.renderer.layers.each(layer -> table.add(new LayerButton(layer)).size(128f).pad(4f, 16f, 0f, 16f).row());
    }

    // region subclasses

    public static class SwapButton extends ImageButton {

        public SwapButton(Color first, Color second, float x, float y) {
            super(Icons.swap, Styles.imageNoneStyle);
            setTranslation(x, y);

            clicked(() -> {
                var temp = first.cpy();
                first.set(second.cpy());
                second.set(temp);

                ui.showInfoToast(Icons.swap, "@swapped");
            });
        }
    }

    public static class ColorBlob extends ImageButton {

        public ColorBlob(Color color, float x, float y) {
            super(Drawables.color_blob, Styles.imageNoneStyle);

            setTranslation(x, y);

            clicked(() -> ui.pickerDialog.show(color));
            update(() -> getImage().setColor(color));
        }
    }

    public static class LayerButton extends ImageButton {
        public Layer layer;

        public LayerButton(Layer layer) {
            super(new TextureRegion(layer.getTexture()), Styles.layerImageButtonStyle);
            this.layer = layer;

            resizeImage(120f);

            clicked(() -> {
                editor.renderer.current = layer;
                ui.hudFragment.pane.scrollToY(this.y - 264f);
            });

            update(() -> {
                setChecked(editor.renderer.current == layer);
                getImage().setDrawable(new TextureRegion(layer.getTexture()));
            });
        }
    }

    public static class SideLayerTable extends Table {
        public Layer layer;

        public SideLayerTable(Table parent) {
            parent.add(this);

            defaults().size(43f);
            visible(() ->
                    input.mouseX() > this.x + translation.x &&
                    input.mouseY() > this.y + translation.y &&
                    input.mouseY() < this.y + translation.y + height);

            button(Icons.up, Styles.sideLayerImageButtonStyle, () -> editor.renderer.moveLayer(layer, -1))
                    .visible(() -> editor.renderer.canMove(layer, -1))
                    .tooltip("@layer.move.up").row();

            button(Icons.eraser, Styles.sideLayerImageButtonStyle, () -> editor.renderer.removeLayer(layer))
                    .visible(() -> editor.renderer.canRemove())
                    .tooltip("@layer.remove").row();

            button(Icons.down, Styles.sideLayerImageButtonStyle, () -> editor.renderer.moveLayer(layer, 1))
                    .visible(() -> editor.renderer.canMove(layer, 1))
                    .tooltip("@layer.move.down").row();
        }

        public void show(Layer layer, float ty) {
            this.layer = layer;
            setTranslation(graphics.getWidth() - 136f, ty);
        }

        public void hide() {
            this.layer = null;
            setTranslation(0f, 0f);
        }
    }

    // endregion
}