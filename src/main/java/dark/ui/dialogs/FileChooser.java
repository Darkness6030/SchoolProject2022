package dark.ui.dialogs;

import arc.files.Fi;
import arc.func.Cons;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import dark.ui.Icons;
import dark.ui.elements.FocusScrollPane;

import static arc.Core.*;

import java.util.Comparator;

public class FileChooser extends BaseDialog {

    public static final String externalStorage = files.getExternalStoragePath();
    public static final Fi homeDirectory = files.absolute(externalStorage);

    public FileHistory history = new FileHistory();
    public Fi directory = files.absolute(settings.getString("last-dir", externalStorage));

    public boolean open;
    public String extension;

    public Table list;
    public FocusScrollPane pane;
    public TextField field;

    public FileChooser(String title, boolean open, String extension, Cons<Fi> cons) {
        super(title);
        addCloseButton();

        this.open = open;
        this.extension = extension;

        cont.table(nav -> {
            nav.defaults().size(48f).padRight(8f);

            nav.button(Icons.home, this::openHomeDirectory);
            nav.button(Icons.left, history::back).disabled(b -> history.noBack());
            nav.button(Icons.right, history::forward).disabled(b -> history.noForward());
            nav.button(Icons.up, this::openParentDirectory);

            nav.label(() -> directory.absolutePath()).ellipsis(true).width(318f);
        }).growX().row();

        list = new Table();
        list.top();

        pane = new FocusScrollPane(list);
        pane.setOverscroll(true, true);
        pane.setFadeScrollBars(false);

        cont.add(pane).size(550f, 700f).row();
        cont.table(name -> {
            name.left();

            field = new TextField();
            field.setOnlyFontChars(false);
            field.setDisabled(open);
            field.setMaxLength(128);
            field.setMessageText("@empty");

            name.add("@file.name");
            name.add(field).grow().padLeft(8f);
        }).growX();

        buttons.buttonRow("@ok", Icons.ok, () -> {
            Fi fi = directory.child(name());
            cons.get(open ? fi : fi.sibling(fi.nameWithoutExtension() + "." + extension));

            hide();
        }).disabled(button -> open ? !directory.child(name()).exists() || directory.child(name()).isDirectory() : name().trim().isEmpty());

        if (!directory.exists()) directory = homeDirectory;
        updateFiles(true);
    }

    public String name() {
        return field.getText();
    }

    // region files

    public Seq<Fi> getAvailableFiles() {
        return directory.seq()
                .removeAll(fi -> fi.name().startsWith(".")) // убирает скрытые файлы
                .filter(fi -> fi.isDirectory() || fi.extEquals(extension))
                .sort(Comparator.comparing(Fi::isDirectory))
                .sort(Comparator.comparing(Fi::name));
    }

    public void updateFiles(boolean push) {
        if (push) history.push(directory);
        settings.put("last-dir", directory.absolutePath());

        list.clear();
        list.defaults().height(48f).growX();

        Cons<TextButton> labelAlign = b -> b.getLabelCell().padLeft(8f).labelAlign(Align.left);
        list.button(".." + directory.toString(), Icons.up, this::openParentDirectory).with(labelAlign).row();

        getAvailableFiles().each(fi -> {
            list.button(fi.name().replace("[", "[["), fi.isDirectory() ? Icons.folder : Icons.file, () -> {
                if (fi.isDirectory())
                    openChildDirectory(fi.name());
                else
                    field.setText(fi.name());
            }).checked(b -> name().equals(fi.name())).with(labelAlign).row();
        });

        pane.setScrollY(0f);
        if (open) field.clearText();
    }

    // endregion
    // region directories

    public void openHomeDirectory() {
        directory = homeDirectory;
        updateFiles(true);
    }

    public void openParentDirectory() {
        directory = directory.parent();
        updateFiles(true);
    }

    public void openChildDirectory(String name) {
        directory = directory.child(name);
        updateFiles(true);
    }

    // endregion

    public class FileHistory extends Seq<Fi> {

        public int index;

        public void push(Fi file) {
            if (index != size) truncate(index); // новая папка открыта не из конца истории

            add(file);
            index++;
        }

        public void back() {
            directory = get(--index - 1);
            updateFiles(false);
        }

        public void forward() {
            directory = get(index++);
            updateFiles(false);
        }

        public boolean noForward() {
            return index >= size;
        }

        public boolean noBack() {
            return index <= 1;
        }
    }
}
