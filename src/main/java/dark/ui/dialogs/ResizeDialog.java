package dark.ui.dialogs;

import arc.util.Align;
import dark.ui.Icons;
import dark.ui.Styles;
import dark.ui.elements.Field;

import static dark.Main.*;

public class ResizeDialog extends BaseDialog {

    public static final int maxSize = 4096;

    public int width, height;
    public boolean scale = true;
    public int align = Align.center;

    public ResizeDialog() {
        super("@resize");

        addCloseButton();
        addConfirmButton(() -> {
            editor.resize(width, height, scale, true, align); // TODO добавить switch для выключения фильтрации
            hide();
        });

        shown(() -> {
            width = canvas.width;
            height = canvas.height;

            cont.clear();

            cont.table(size -> {
                size.defaults().width(196f).padTop(8f);

                size.add(new Field("@width", 96f, width, 4, 1, maxSize, (int value) -> width = value)).row();
                size.add(new Field("@height", 96f, height, 4, 1, maxSize, (int value) -> height = value));
            }).top();

            cont.table(type -> {
                type.table(tabs -> {
                    tabs.defaults().grow();

                    tabs.button("@scale", Styles.layersTab, () -> scale = true).checked(t -> scale);
                    tabs.button("@align", Styles.historyTab, () -> scale = false).checked(t -> !scale);
                }).size(196f, 32f).pad(2f, 0f, 6f, 0f).row();

                type.table(align -> {
                    align.defaults().size(32f).pad(4f);

                    for (int value : new int[] {
                            Align.topLeft, Align.top, Align.topRight,
                            Align.left, Align.center, Align.right,
                            Align.bottomLeft, Align.bottom, Align.bottomRight
                    }) {
                        align.button(button -> {}, Styles.align, () -> this.align = value).checked(button -> this.align == value);
                        if (value >= 16) align.row();
                    }
                }).top().visible(() -> !scale);
            });
        });
    }
}