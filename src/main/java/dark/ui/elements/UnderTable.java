package dark.ui.elements;

import arc.func.Cons;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.scene.Element;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Align;
import arc.util.Tmp;
import dark.ui.Drawables;
import dark.ui.Palette;

import static arc.Core.*;

public class UnderTable extends Table {

    public static Table root;
    public Element parent;

    public UnderTable(Element parent, Cons<Table> cons) {
        super(Drawables.main_rounded);

        this.parent = parent;
        cons.get(this);

        update(() -> { // прячем элемент, если курсор уходит слишком далеко
            if (!Tmp.r1.setCentered(translation.x, translation.y, width, height).grow(64f).contains(input.mouse())) root.clear();
        });
    }

    public static void build(WidgetGroup parent) {
        parent.fill(cont -> {
            cont.name = "Under table";
            cont.fillParent = false;
            root = cont;
        });
    }

    public void show() {
        root.clear();
        root.add(this);
        root.layout();

        var pos = parent.localToStageCoordinates(Tmp.v1.set(parent.getWidth() / 2f, -height / 2f - 10f));
        setTranslation(pos.x, pos.y);
    }

    @Override
    public void draw() {
        Fill.dropShadowRect(x, y, width, height, 48f, .4f); // rect чтобы самому не высчитывать центр элемента

        Draw.color(Palette.main);
        float x = getX(Align.top), y = getY(Align.top);
        Fill.tri(x - 10f, y, x, y + 10f, x + 10f, y);

        super.draw();
    }
}