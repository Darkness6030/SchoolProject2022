package dark.ui.dialogs;

import arc.files.Fi;
import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.g2d.GlyphLayout;
import arc.input.KeyCode;
import arc.scene.event.Touchable;
import arc.scene.ui.*;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.pooling.Pools;
import dark.ui.Fonts;
import dark.ui.Icon;
import dark.ui.Styles;

import java.util.Arrays;

import static arc.Core.*;
import static java.lang.String.CASE_INSENSITIVE_ORDER;

public class FileChooser extends BaseDialog {

    public static final Fi homeDirectory = files.absolute(files.getExternalStoragePath());
    public static Fi lastDirectory = files.absolute(settings.getString("lastDirectory", homeDirectory.absolutePath()));

    public Fi directory = lastDirectory;
    public Table filesTable;
    public ScrollPane pane;
    public TextField navigation, filesField;
    public TextButton ok;
    public final FileHistory stack = new FileHistory();
    public final Boolf<Fi> filter;
    public final Cons<Fi> selectListener;
    public final boolean open;

    public FileChooser(String title, Boolf<Fi> filter, boolean open, Cons<Fi> result) {
        super(title);
        setFillParent(true);

        this.open = open;
        this.filter = filter;
        this.selectListener = result;

        if (!lastDirectory.exists()) {
            lastDirectory = homeDirectory;
            directory = lastDirectory;
        }

        shown(() -> {
            cont.clear();
            setupWidgets();
        });

        keyDown(KeyCode.enter, () -> ok.fireClick());
        closeOnBack();
    }

    public void setupWidgets() {
        cont.margin(-10);

        filesField = new TextField();
        filesField.setOnlyFontChars(false);
        if (!open) filesField.addInputDialog();
        filesField.setDisabled(open);

        ok = new TextButton(open ? "@load" : "@save");

        ok.clicked(() -> {
            if (ok.isDisabled()) return;
            if (selectListener != null)
                selectListener.get(directory.child(filesField.getText()));
            hide();
        });

        filesField.changed(() -> ok.setDisabled(filesField.getText().isBlank()));
        filesField.change();

        var cancel = new TextButton("@cancel");
        cancel.clicked(this::hide);

        navigation = new TextField("");
        navigation.touchable = Touchable.disabled;

        filesTable = new Table();
        filesTable.marginRight(10);
        filesTable.marginLeft(3);

        pane = new ScrollPane(filesTable);
        pane.setOverscroll(false, false);
        pane.setFadeScrollBars(false);

        updateFiles(true);

        var up = new ImageButton(Icon.up);
        up.clicked(() -> {
            directory = directory.parent();
            updateFiles(true);
        });

        var back = new ImageButton(Icon.left);
        var forward = new ImageButton(Icon.right);

        forward.clicked(stack::forward);
        back.clicked(stack::back);
        forward.setDisabled(stack::notForward);
        back.setDisabled(stack::notBack);

        var home = new ImageButton(Icon.folder);
        home.clicked(() -> {
            directory = homeDirectory;
            setLastDirectory(directory);
            updateFiles(true);
        });

        var icons = new Table();
        icons.defaults().height(60).growX().padTop(5).uniform();
        icons.add(home);
        icons.add(back);
        icons.add(forward);
        icons.add(up);

        var fileName = new Table();
        fileName.bottom().left().add(new Label("@filename"));
        fileName.add(filesField).height(40f).fillX().expandX().padLeft(10f);

        var buttons = new Table();
        buttons.defaults().growX().height(60);
        buttons.add(cancel);
        buttons.add(ok);

        var content = new Table().top().left();
        content.add(icons).expandX().fillX();
        content.row();

        content.center().add(pane).colspan(3).grow();
        content.row();

        if (!open) {
            content.bottom().left().add(fileName).colspan(3).grow().padTop(-2).padBottom(2);
            content.row();
        }

        cont.add(content.add(buttons).growX().get()).grow();
    }

    public void updateFileFieldStatus() {
        if (!open) {
            ok.setDisabled(filesField.getText().replace(" ", "").isEmpty());
        } else {
            ok.setDisabled(!directory.child(filesField.getText()).exists() || directory.child(filesField.getText()).isDirectory());
        }
    }

    public Seq<Fi> getFileNames() {
        var files = directory.list(file -> !file.getName().startsWith("."));

        Arrays.sort(files, (a, b) -> {
            if (a.isDirectory() && !b.isDirectory()) return -1;
            if (!a.isDirectory() && b.isDirectory()) return 1;

            return CASE_INSENSITIVE_ORDER.compare(a.name(), b.name());
        });

        return Seq.with(files);
    }

    public void updateFiles(boolean push) {
        if (push) stack.push(directory);
        navigation.setText(directory.toString());

        var layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
        layout.setText(Fonts.def, navigation.getText());

        if (layout.width < navigation.getWidth()) navigation.setCursorPosition(0);
        else navigation.setCursorPosition(navigation.getText().length());

        Pools.free(layout);

        filesTable.clearChildren();
        filesTable.top().left();

        var names = getFileNames();

        var imageUp = new Image(Icon.up);
        var buttonUp = new TextButton(".." + directory.toString(), Styles.checkTextButtonStyle);

        buttonUp.clicked(() -> {
            directory = directory.parent();
            setLastDirectory(directory);
            updateFiles(true);
        });

        buttonUp.left().add(imageUp).padRight(4f).padLeft(4);
        buttonUp.getLabel().setAlignment(Align.left);
        buttonUp.getCells().reverse();

        filesTable.add(buttonUp).align(Align.topLeft).fillX().expandX().height(50).pad(2).colspan(2);
        filesTable.row();

        var group = new ButtonGroup<>();
        group.setMinCheckCount(0);

        names.each(fi -> {
            if (!fi.isDirectory() && !filter.get(fi)) return;

            var name = fi.name();
            var button = new TextButton(name.replace("[", "[["), Styles.checkTextButtonStyle);

            button.getLabel().setWrap(false);
            button.getLabel().setEllipsis(true);
            group.add(button);

            button.clicked(() -> {
                if (!fi.isDirectory()) {
                    filesField.setText(name);
                    updateFileFieldStatus();
                } else {
                    setLastDirectory(directory = directory.child(name));
                    updateFiles(true);
                }
            });

            filesField.changed(() -> button.setChecked(name.equals(filesField.getText())));
            button.add(new Image(fi.isDirectory() ? Icon.folder : Icon.file)).padRight(4f).padLeft(4);
            button.getCells().reverse();

            filesTable.top().left().add(button).align(Align.topLeft).fillX().expandX().height(50).pad(2).padTop(0).padBottom(0).colspan(2);

            button.getLabel().setAlignment(Align.left);
            filesTable.row();
        });

        pane.setScrollY(0f);
        updateFileFieldStatus();

        if (open) filesField.clearText();
    }

    public static void setLastDirectory(Fi directory) {
        lastDirectory = directory;
        settings.put("lastDirectory", directory.absolutePath());
    }

    public class FileHistory {
        public final Seq<Fi> history = new Seq<>();
        public int index;

        public void push(Fi file) {
            if (index != history.size) history.truncate(index++);
            history.add(file);
        }

        public void back() {
            if (notBack()) return;

            setLastDirectory(directory = history.get(index -= 2));
            updateFiles(false);
        }

        public void forward() {
            if (notForward()) return;

            setLastDirectory(directory = history.get(index++));
            updateFiles(false);
        }

        public boolean notForward() {
            return index >= history.size;
        }

        public boolean notBack() {
            return index == 1 || index <= 0;
        }
    }
}