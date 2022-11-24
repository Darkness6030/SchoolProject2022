package dark.ui.dialogs;

import arc.Core;
import arc.files.Fi;
import arc.func.Cons;
import dark.ui.Icons;

public class ExplorerDialog extends BaseDialog {

    protected Runnable callback;
    protected boolean create;

    public Fi currentFolder, selectedFile;
    public String input = "";

    public ExplorerDialog() {
        super("Explorer");
        currentFolder = Core.files.local(""); // move to current folder
    }

    public void rebuild(boolean create) {
        buttons.clear();
        addCloseButton();

        if (create)
            addButton(Icons.save, "Save", callback::run).disabled(button -> input.isEmpty());
        else
            addButton(Icons.load, "Load", callback::run).disabled(button -> selectedFile == null);

        rebuildList();

    }

    public void rebuildList() {
        cont.clear();
        cont.defaults().left();

        cont.button(Icons.folder + " " + currentFolder.absolutePath(), () -> {
            currentFolder = currentFolder.parent();
            rebuildList();
        });

        currentFolder.seq().each(file -> {
            cont.row();
            cont.button(Icons.file + " " + file.name(), () -> {
                if (file.isDirectory()) currentFolder = file;
                else currentFile(file);
                rebuildList();
            });
        });
    }

    public Fi currentFile() {
        return create ? currentFolder.child(input) : selectedFile;
    }

    public void currentFile(Fi file) {
        if (create) input = file.name();
        else selectedFile = file;
    }

    public void show(Cons<Fi> callback, boolean create) {
        this.callback = () -> {
            callback.get(currentFile());
            hide();
        };
        this.create = create;

        this.selectedFile = null;

        rebuild(create);
        super.show();
    }

}
