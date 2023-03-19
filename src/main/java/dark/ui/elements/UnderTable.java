package dark.ui.elements;

import arc.func.Cons;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import dark.ui.Drawables;
import dark.ui.Palette;

import static dark.Main.*;

public class UnderTable extends Table {

    public Element parent;

    public UnderTable(Element parent, Cons<Table> cons) {
        super(Drawables.main_rounded);
        this.parent = parent;

        visible = false;
        parent.hovered(this::show);
        parent.exited(() -> visible = false);

        ui.hud.parent.fill(cont -> {
            cont.name = "Under table";
            cont.fillParent = false;

            cons.get(this);
            cont.add(this);
        });
    }

    public void show() {
        var pos = parent.localToStageCoordinates(new Vec2(parent.getWidth() / 2f, -height / 2f - 10f));
        setTranslation(pos.x, pos.y);

        visible = true;
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
