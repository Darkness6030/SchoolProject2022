package dark.ui.dialogs;

import arc.files.Fi;
import arc.func.Cons;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Structs;
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

    public final boolean open;
    public final Seq<String> extensions;

    public Table list;
    public FocusScrollPane pane;
    public TextField field;

    public FileChooser(String title, Seq<String> extensions, Cons<Fi> cons) {
        this(title, true, extensions, cons);
    }

    public FileChooser(String title, String extension, Cons<Fi> cons) {
        this(title, false, Seq.with(extension), file -> cons.get(file.sibling(file.nameWithoutExtension() + "." + extension)));
    }

    public FileChooser(String title, boolean open, Seq<String> extensions, Cons<Fi> cons) {
        super(title);

        this.open = open;
        this.extensions = extensions;

        addCloseButton();
        addConfirmButton(() -> {
            cons.get(directory.child(field.getText()));
            hide();
        }, () -> {
            String name = field.getText(); // если заменишь на var, я тебя зарежу
            return open ? !directory.child(name).exists() || directory.child(name).isDirectory() : name.trim().isEmpty();
        });

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
                .filter(file -> !file.file().isHidden()) // убираем скрытые файлы
                .filter(file -> file.isDirectory() || extensions.contains(file::extEquals))
                .sort(Structs.comps(Comparator.comparing(file -> !file.isDirectory()), Comparator.comparing(file -> file.name().toLowerCase())));
    }

    public void updateFiles(boolean push) {
        if (push) history.push(directory);
        settings.put("last-dir", directory.absolutePath());

        list.clear();
        list.defaults().height(48f).growX();

        Cons<TextButton> labelAlign = button -> button.getLabelCell().padLeft(8f).labelAlign(Align.left);
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