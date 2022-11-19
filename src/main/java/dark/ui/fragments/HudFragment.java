package dark.ui.fragments;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.Seq;
import dark.editor.EditType;
import dark.ui.Icons;
import dark.ui.Textures;
import dark.ui.elements.LayerButton;
import dark.ui.elements.TextSlider;

import static dark.Main.editor;
import static dark.Main.ui;

public class HudFragment {

    public final Seq<LayerButton> layerButtons = new Seq<>();

    public void build(WidgetGroup parent) {
        parent.fill(content -> {
            content.name = "Menu Bar";
            content.top();

            content.table(Textures.underline, table -> {
                table.left();

                table.button("Menu", () -> ui.menuDialog.show()).size(64f).pad(32f);

                new TextSlider(1f, 100f, 1f, editor.drawSize, value -> (editor.drawSize = value.intValue()) + "px").build(table).padRight(32f);

                table.stack(
                        new Table(color -> color.button(String.valueOf(Icons.swap), () -> {
                            var temp = editor.first.cpy();
                            editor.first.set(editor.second);
                            editor.second.set(temp);
                        }).size(16f).with(button -> button.setTranslation(18f, 18f))),
                        new ColorBlob(editor.second, 8f, -8f),
                        new ColorBlob(editor.first, -8f, 8f)
                ).size(64f).padRight(32f);

                table.label(() -> "Layer " + (editor.canvas.currentLayer + 1) + "/" + editor.canvas.layers.size);
            }).height(64f).growX();
        });

        parent.fill(cont -> {
            cont.name = "Tools Bar";
            cont.left();

            cont.table(Textures.sideline, pad -> {
                pad.top();

                for (var type : EditType.values())
                    type.button(pad);
            }).width(64f).growY().padTop(64f);
        });

        parent.fill(cont -> {
            cont.name = "Layers";
            cont.right();

            cont.table(Textures.sideline_left, pad -> {
                pad.top().marginLeft(8f);
                pad.defaults().size(128f).padBottom(4f);

                pad.update(() -> {
                    if (layerButtons.size == editor.canvas.layers.size) return;

                    layerButtons.clear();
                    editor.canvas.layers.each(layer -> {
                        var button = new LayerButton(layer);
                        button.clicked(() -> editor.canvas.layer(layer));

                        layerButtons.add(button);
                    });

                    pad.clear();
                    layerButtons.each(button -> pad.add(button).row());
                });
            }).growY().padTop(64f);
        });
    }

    public static class ColorBlob extends Table {

        public ColorBlob(Color color, float x, float y) {
            button(Textures.color_blob, () -> ui.pickerDialog.show(color))
                    .with(button -> button.setTranslation(x, y))
                    .update(button -> button.getImage().setColor(color));
        }
    }
}