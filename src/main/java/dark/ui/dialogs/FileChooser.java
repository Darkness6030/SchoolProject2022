package dark.ui.dialogs;

import arc.Core;
import arc.files.Fi;
import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.g2d.GlyphLayout;
import arc.scene.event.Touchable;
import arc.scene.ui.*;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import dark.ui.*;

import java.util.Comparator;

public class FileChooser extends BaseDialog {
    public static final Fi homeDirectory = Core.files.absolute(Core.files.getExternalStoragePath());

    public Fi directory = homeDirectory;
    public Table table;
    public ScrollPane pane;
    public TextField field, navigation;

    public final FileHistory history = new FileHistory();
    public final Boolf<Fi> filter;
    public final boolean open;
    public final Cons<Fi> result;

    public FileChooser(String title, Boolf<Fi> filter, boolean open, Cons<Fi> result) {
        super(title);
        setFillParent(true);

        this.open = open;
        this.filter = filter;
        this.result = result;

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

        navigation = new TextField();
        navigation.touchable = Touchable.disabled;

        updateFiles(true);

        var icons = new Table();
        icons.defaults().growX().height(60f).padTop(5f).uniform();
        icons.button(Icons.home, () -> {
            directory = homeDirectory;
            updateFiles(true);
        });

        icons.button(Icons.left, history::back).disabled(button -> history.noBack());
        icons.button(Icons.right, history::forward).disabled(button -> history.noForward());
        icons.button(Icons.up, () -> {
            directory = directory.parent();
            updateFiles(true);
        });

        var fileName = new Table();
        fileName.bottom().left().add(new Label("@file.name"));
        fileName.add(field).growX().height(40f).padLeft(10f);

        buttons.defaults().grow().height(60f);
        buttons.button("@cancel", this::hide);
        buttons.button("@ok", () -> {
            result.get(directory.child(field.getText()));
            hide();
        }).disabled(button -> open ? !directory.child(field.getText()).exists() || directory.child(field.getText()).isDirectory() : field.getText().isBlank());

        cont.top().left().add(icons).growX();
        cont.row();

        cont.center().add(pane).colspan(3).grow();
        cont.row();

        cont.bottom().left().add(fileName).colspan(3).grow().padTop(-2f).padBottom(2f);
        cont.row();

        cont.add(buttons).grow().fill();
    }

    public Seq<Fi> getAvailableFiles() {
        return directory.seq()
                .filter(fi -> fi.isDirectory() || filter.get(fi))
                .sort(Comparator.comparing(Fi::isDirectory))
                .sort(Comparator.comparing(Fi::name));
    }

    public void updateFiles(boolean push) {
        if (push) history.push(directory);
        navigation.setText(directory.toString());

        var layout = new GlyphLayout();
        layout.setText(Fonts.def, navigation.getText());

        navigation.setCursorPosition(layout.width < navigation.getWidth() ? 0 : navigation.getText().length());

        table.clearChildren();
        table.top().left();

        var up = table.button(".." + directory.toString(), Styles.checkTextButtonStyle, () -> {
            directory = directory.parent();
            updateFiles(true);
        }).align(Align.topLeft).colspan(2).growX().height(50f).pad(2f).get();

        up.image(Icons.up).padLeft(4f).padRight(4f);
        up.getLabel().setAlignment(Align.left);
        up.getCells().reverse();

        table.row();
        table.top().left();

        getAvailableFiles().each(fi -> {
            var button = table.button(fi.name().replace("[", "[["), Styles.checkTextButtonStyle, () -> {
                if (fi.isDirectory()) {
                    directory = directory.child(fi.name());
                    updateFiles(true);
                } else field.setText(fi.name());
            }).checked(textButton -> field.getText().equals(fi.name()))
                    .align(Align.topLeft).colspan(2).growX()
                    .height(50f).padLeft(2f).padRight(2f)
                    .get();

            button.image(fi.isDirectory() ? Icons.folder : Icons.file).padLeft(4f).padRight(4f);
            button.getLabel().setAlignment(Align.left);
            button.getCells().reverse();

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