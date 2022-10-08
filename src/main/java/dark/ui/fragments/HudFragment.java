package dark.ui.fragments;

import arc.scene.ui.layout.WidgetGroup;
import dark.editor.EditType;
import dark.ui.Textures;
import dark.ui.elements.TextSlider;

import static dark.Main.*;

public class HudFragment {

    public void build(WidgetGroup parent) {
        parent.fill(cont -> {
            cont.name = "Menu Bar";
            cont.top();

            cont.table(Textures.alpha_bg, pad -> {
                pad.left();
                pad.defaults().pad(4f);

                for (var type : EditType.values())
                    type.button(pad);

                new TextSlider(1f, 100f, 1f, 2f, value -> "[darkgray]x" + (editor.canvas.drawSize = value)).build(pad);
            }).height(40f).growX();
        });
    }
}
