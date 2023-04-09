package dark.ui.dialogs;

import arc.files.Fi;
import arc.func.Cons;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import dark.ui.Icons;
import dark.ui.Styles;
import dark.ui.elements.FocusScrollPane;

import java.util.Comparator;

import static arc.Core.files;
import static arc.Core.settings;

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
        addConfirmButton(() -> { // TODO должен быть способ получше чем передавать Boolp
            var file = directory.child(field.getText());
            cons.get(open ? file : file.sibling(file.nameWithoutExtension() + "." + extension));

            hide();
        }, () -> {
            String name = field.getText(); // если заменишь на var, я тебя зарежу
            return open ? !directory.child(name).exists() || directory.child(name).isDirectory() : name.trim().isEmpty();
        });

        this.open = open;
        this.extension = extension;

        cont.table(nav -> {
            nav.left();
            nav.defaults().size(48f).padRight(8f);

            nav.button(Icons.home, this::openHomeDirectory);
            nav.button(Icons.left, history::back).disabled(button -> !history.hasBack());
            nav.button(Icons.right, history::forward).disabled(button -> !history.hasForward());
            nav.button(Icons.up, this::openParentDirectory);
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

        if (!directory.exists()) directory = homeDirectory;
        updateFiles(true);
    }

    // region files

    public Seq<Fi> getAvailableFiles() {
        return directory.seq()
                .filter(file -> !file.name().startsWith(".")) // убирает скрытые файлы
                .filter(file -> file.isDirectory() || file.extEquals(extension))
                .sort(Comparator.comparing(Fi::isDirectory))
                .sort(Comparator.comparing(file -> file.name().toLowerCase()));
    }

    public void updateFiles(boolean push) {
        if (push) history.push(directory);
        settings.put("last-dir", directory.absolutePath());

        list.clear();
        list.defaults().height(48f).growX();

        Cons<TextButton> labelAlign = b -> b.getLabelCell().padLeft(8f).labelAlign(Align.left);
        list.button(directory.toString(), Icons.up, this::openParentDirectory).with(labelAlign).row();

        getAvailableFiles().each(file ->
            list.button(file.name(), file.isDirectory() ? Icons.folder : Icons.file, Styles.textButtonCheck, () -> {
                if (file.isDirectory())
                    openChildDirectory(file.name());
                else
                    field.setText(file.name());
            }).checked(b -> field.getText().equals(file.name())).with(labelAlign).row());

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

        public static final int max = 64;
        public int index;

        public void push(Fi file) {
            if (index != size) truncate(index); // новая папка открыта не из конца истории
            add(file);

            if (size > max) remove(0);
            else index++;
        }

        public void back() {
            directory = get(--index - 1);
            updateFiles(false);
        }

        public void forward() {
            directory = get(index++);
            updateFiles(false);
        }

        public boolean hasBack() {
            return index > 1;
        }

        public boolean hasForward() {
            return index < size;
        }
    }
}