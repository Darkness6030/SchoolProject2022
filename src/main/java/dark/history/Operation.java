package dark.history;

import arc.scene.style.Drawable;

public interface Operation {

    public void undo();
    public void redo();

    public Drawable icon();
    public String name();
    public String desc();
}
