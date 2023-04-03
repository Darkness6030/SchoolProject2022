package dark.ui;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.func.Cons;
import arc.input.KeyCode;
import arc.math.Interp;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.style.Drawable;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.Align;
import dark.ui.dialogs.*;
import dark.ui.elements.*;
import dark.ui.fragments.*;

import static arc.Core.*;

public class UI implements ApplicationListener {

    public final WidgetGroup hud = new WidgetGroup();

    public final HudFragment hudFragment = new HudFragment();
    public final ColorWheel colorWheel = new ColorWheel();

    public MenuDialog menu;
    public PaletteDialog palette;
    public ResizeDialog resize;
    public NewCanvasDialog newCanvas;

    public Table toast;

    @Override
    public void init() {
        input.addProcessor(scene);
        scene.add(hud);

        hud.setFillParent(true);
        hud.touchable = Touchable.childrenOnly;

        hudFragment.build(hud);
        colorWheel.build(hud);
        UnderTable.build(hud);

        menu = new MenuDialog();
        palette = new PaletteDialog();
        resize = new ResizeDialog();
        newCanvas = new NewCanvasDialog();
    }

    @Override
    public void update() {
        scene.act();
        scene.draw();

        if (input.keyTap(KeyCode.escape) && !scene.hasDialog() && !menu.isShown()) menu.show();

        if ((input.keyTap(KeyCode.mouseLeft) || input.keyTap(KeyCode.mouseRight)) && scene.hasField()) {
            var element = scene.hit(input.mouseX(), input.mouseY(), true);
            if (!(element instanceof TextField || (element instanceof Slider slider && slider.parent instanceof SliderTable) || (element instanceof Label label && label.parent instanceof Table table && table.parent instanceof Field)))
                scene.setKeyboardFocus(null);
        }
    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width, height);
    }

    public void showFileChooser(String title, boolean open, String extension, Cons<Fi> cons) {
        new FileChooser(title, open, extension, cons).show();
    }

    public void showInfoToast(Drawable icon, String text) {
        Sounds.message.play();

        if (toast != null) toast.remove();

        toast = scene.table();
        toast.touchable = Touchable.disabled;

        toast.table(Drawables.main_rounded, table -> {
            table.image(icon).size(32f);
            table.add(text).wrap().size(280f, 32f).labelAlign(Align.center);
        });

        toast.color.a(0f);
        toast.setTranslation(0, 48f);
        toast.actions(
                Actions.parallel(Actions.alpha(1f, .5f, Interp.fastSlow), Actions.translateBy(0, -48f, .5f, Interp.fastSlow)),
                Actions.delay(1f),
                Actions.parallel(Actions.alpha(0f, .5f, Interp.slowFast), Actions.translateBy(0, -48f, .5f, Interp.slowFast)),
                Actions.remove());
    }
}