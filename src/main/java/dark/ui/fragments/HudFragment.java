package dark.ui.fragments;

import arc.graphics.Color;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Tmp;
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
                pad.top();
                pad.button("@layer.new", editor::newLayer)
                        .disabled(button -> !editor.renderer.canAdd())
                        .tooltip(bundle.format("layer.new.tooltip", Renderer.maxLayers))
                        .width(128f).padTop(8f).padBottom(8f).row();

                pane = new FocusScrollPane(layers = new Table().top());
                pane.setScrollingDisabledX(true);

                pane.setOverscroll(true, true);
                pane.setFadeScrollBars(true);

                pad.add(pane).height(528f);

                updateLayers();
            }).width(196f).growY().padTop(64f);
        });
       
        parent.fill(cont -> { // corners
            cont.name = "Corners";
            cont.top();

            cont.image(Drawables.corners).height(7f).growX().pad(64f, 64f, 0f, 196f);
        });
    }

    public void updateLayers() {
        if (layers == null) return;

        layers.clear();
        editor.renderer.layers.each(layer -> layers.add(new LayerButton(layer)).size(128f).pad(4f, 16f, 0f, 16f).row());
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
            super(layer.region, Styles.layerImageButtonStyle);
            this.layer = layer;

            resizeImage(120f);

            clicked(() -> {
                editor.renderer.current = layer;
                ui.hudFragment.pane.scrollToY(this.y - 264f);
            });
            update(() -> setChecked(editor.renderer.current == layer));
        }
    }

    // endregion
}
