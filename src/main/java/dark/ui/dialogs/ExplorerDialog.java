package dark.ui.dialogs;

import arc.Core;
import arc.files.Fi;
import arc.func.Cons;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import dark.ui.Icons;

public class ExplorerDialog extends BaseDialog {

    protected Table list;
    protected TextField input;

    protected Runnable callback;
    protected boolean create;

    public Fi currentFolder, selectedFile;

    public ExplorerDialog() {
        super("Explorer");

        cont.table(table -> list = table).padBottom(16f).row();
        cont.table(field -> {
            field.add("File Name: ");
            input = field.field("", null).get();
        });

        currentFolder = Core.files.local(""); // move to current folder
    }

    public void rebuild(boolean create) {
        buttons.clear();
        addCloseButton();

        if (create)
            addButton(Icons.save, "Save", callback::run).disabled(button -> input.getText().isEmpty());
        else
            addButton(Icons.load, "Load", callback::run).disabled(button -> selectedFile == null);

        rebuildList();
    }

    public void rebuildList() {
        list.clear();

        list.button(Icons.folder + " " + currentFolder.absolutePath(), () -> {
            currentFolder = currentFolder.parent();
            rebuildList();
        }).row();

        currentFolder.seq().each(file -> {
            list.button(Icons.file + " " + file.name(), () -> {
                if (file.isDirectory()) currentFolder = file;
                else currentFile(file);
                rebuildList();
            }).row();
        });
    }

    public Fi currentFile() {
        return create ? currentFolder.child(input.getText()) : selectedFile;
    }

    public void currentFile(Fi file) {
        if (create) input.setText(file.name());
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
