package dark.ui.dialogs;

import arc.files.Fi;
import arc.func.Cons;
import arc.scene.event.Touchable;
import arc.scene.ui.*;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import dark.ui.Icons;
import dark.ui.Styles;

import java.util.Comparator;

import static arc.Core.*;

public class FileChooser extends BaseDialog {
    public static final Fi homeDirectory = files.absolute(files.getExternalStoragePath());

    public Fi directory = files.absolute(settings.getString("lastDirectory", homeDirectory.absolutePath()));
    public Table table;
    public ScrollPane pane;
    public TextField field, navigation;

    public final FileHistory history = new FileHistory();

    public final String extension;
    public final boolean open;
    public final Cons<Fi> result;

    public FileChooser(String title, String extension, boolean open, Cons<Fi> result) {
        super(title);
        setFillParent(true);

        this.open = open;
        this.extension = extension;
        this.result = result;

        if (!directory.exists())
            directory = homeDirectory;

        shown(() -> {
            cont.clear();
            setupWidgets();
        });

        closeOnBack();
    }

    public void setupWidgets() {
        table = new Table();
        table.marginRight(10);
        table.marginLeft(3);

        pane = new ScrollPane(table);
        pane.setOverscroll(false, false);
        pane.setFadeScrollBars(false);

        field = new TextField();
        field.setOnlyFontChars(false);
        field.setDisabled(open);
        field.setMaxLength(128);
        field.setFilter((field, text) -> text != ' ');
        field.setMessageText("@empty");

        navigation = new TextField();
        navigation.touchable = Touchable.disabled;

        updateFiles(true);

        var icons = new Table();
        icons.defaults().growX().height(60f).padTop(5f).uniform();

        icons.button(Icons.home, this::openHomeDirectory);
        icons.button(Icons.left, history::back).disabled(button -> history.noBack());
        icons.button(Icons.right, history::forward).disabled(button -> history.noForward());
        icons.button(Icons.up, this::openParentDirectory);

        var fileName = new Table();
        fileName.bottom().left().add(new Label("@file.name"));
        fileName.add(field).height(40f).fillX().expandX().padLeft(12f);

        var buttons = new Table();
        buttons.defaults().grow().height(60f);
        buttons.button("@cancel", this::hide);
        buttons.button("@ok", () -> {
            hide();

            var fi = directory.child(field.getText());
            result.get(fi.sibling(fi.nameWithoutExtension() + "." + extension));
        }).disabled(button -> open ? !directory.child(field.getText()).exists() || directory.child(field.getText()).isDirectory() : field.getText().trim().isEmpty());

        var content = new Table();
        content.top().left().add(icons).growX();
        content.row();

        content.center().add(pane).colspan(3).grow();
        content.row();

        content.bottom().left().add(fileName).colspan(3).grow().padTop(-2f).padBottom(2f);
        content.row();

        content.add(buttons).growX();
        cont.add(content).grow();
    }

    public Seq<Fi> getAvailableFiles() {
        return directory.seq()
                .filter(fi -> fi.isDirectory() || fi.extEquals(extension))
                .sort(Comparator.comparing(Fi::isDirectory))
                .sort(Comparator.comparing(Fi::name));
    }

    public void openHomeDirectory() {
        directory = homeDirectory;
        settings.put("lastDirectory", directory.absolutePath());

        updateFiles(true);
    }

    public void openParentDirectory() {
        directory = directory.parent();
        settings.put("lastDirectory", directory.absolutePath());

        updateFiles(true);
    }

    public void openChildDirectory(String name) {
        directory = directory.child(name);
        settings.put("lastDirectory", directory.absolutePath());

        updateFiles(true);
    }

    public void updateFiles(boolean push) {
        if (push) history.push(directory);
        navigation.setText(directory.toString());

        table.clear();
        table.top().left();

        table.button(".." + directory.toString(), Styles.checkTextButtonStyle, this::openParentDirectory).with(button -> {
            button.image(Icons.up).padLeft(4f).padRight(4f);
            button.getLabel().setAlignment(Align.left);
            button.getCells().reverse();
        }).align(Align.topLeft).colspan(2).growX().height(50f).pad(2f);

        table.row();

        getAvailableFiles().each(fi -> {
            table.button(fi.name().replace("[", "[["), Styles.checkTextButtonStyle, () -> {
                if (fi.isDirectory()) openChildDirectory(fi.name());
                else field.setText(fi.name());
            }).with(button -> {
                button.image(fi.isDirectory() ? Icons.folder : Icons.file).padLeft(4f).padRight(4f);
                button.getLabel().setAlignment(Align.left);
                button.getCells().reverse();
            }).checked(button -> field.getText().equals(fi.name()))
                    .align(Align.topLeft).colspan(2).growX()
                    .height(50f).padLeft(2f).padRight(2f);

            table.row();
        });

        pane.setScrollY(0f);
        if (open) field.clearText();
    }

    public class FileHistory extends Seq<Fi> {
        public int index;

        public void push(Fi file) {
            if (index != size) truncate(index);

            add(file);
            index++;
        }

        public void back() {
            if (noBack()) return;

            directory = get(--index - 1);
            updateFiles(false);
        }

        public void forward() {
            if (noForward()) return;

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