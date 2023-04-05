package dark.history;

import arc.scene.style.Drawable;

public interface Operation {

    void undo();
    void redo();

    Drawable icon();
    String name();
    String desc();
}