package dark.ui.elements;

import arc.scene.Element;
import arc.scene.ui.ScrollPane;

import static arc.Core.scene;

public class FocusScrollPane extends ScrollPane {

    public FocusScrollPane(Element widget) {
        super(widget);

        update(() -> {
            if (!hasMouse() && hasScroll()) scene.setScrollFocus(null);
        });
    }

    public void scrollTo(float x, float y) {
        scrollTo(x, y, getScrollWidth(), getScrollHeight());
    }
}