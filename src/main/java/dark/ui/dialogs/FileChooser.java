package dark.ui.dialogs;

import arc.Core;
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
import dark.ui.*;

import java.util.Arrays;

public class FileChooser extends BaseDialog {
    public static final Fi homeDirectory = Core.files.absolute(Core.files.getExternalStoragePath());
    public static Fi lastDirectory = Core.files.absolute(Core.settings.getString("lastDirectory", homeDirectory.absolutePath()));

    public Fi directory = lastDirectory;
    public Table table;
    public ScrollPane pane;
    public TextField navigation, field;
    public TextButton ok;

    public final FileHistory stack = new FileHistory();
    public final Boolf<Fi> filter;
    public final boolean open;
    public final Cons<Fi> result;

    public FileChooser(String title, Boolf<Fi> filter, boolean open, Cons<Fi> result) {
        super(title);
        setFillParent(true);

        this.open = open;
        this.filter = filter;
        this.result = result;

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

    public static void setLastDirectory(Fi directory) {
        lastDirectory = directory;
        Core.settings.put("lastDirectory", directory.absolutePath());
    }

    private void setupWidgets() {
        cont.margin(-10);

        var content = new Table();

        field = new TextField();
        field.setOnlyFontChars(false);
        if (!open) field.addInputDialog();
        field.setDisabled(open);

        ok = new TextButton(open ? "@load" : "@save");

        ok.clicked(() -> {
            if (ok.isDisabled()) return;
            if (result != null)
                result.get(directory.child(field.getText()));
            hide();
        });

        field.changed(() -> ok.setDisabled(field.getText().isBlank()));
        field.change();

        var cancel = new TextButton("@cancel");
        cancel.clicked(this::hide);

        navigation = new TextField("");
        navigation.touchable = Touchable.disabled;

        table = new Table();
        table.marginRight(10);
        table.marginLeft(3);

        pane = new ScrollPane(table);
        pane.setOverscroll(false, false);
        pane.setFadeScrollBars(false);

        updateFiles(true);

        var iconTable = new Table();

        var up = new ImageButton(Icons.up);
        up.clicked(() -> {
            directory = directory.parent();
            updateFiles(true);
        });

        var back = new ImageButton(Icons.left);
        var forward = new ImageButton(Icons.right);

        forward.clicked(stack::forward);
        back.clicked(stack::back);
        forward.setDisabled(stack::noForward);
        back.setDisabled(stack::noBack);

        var home = new ImageButton(Icons.home);
        home.clicked(() -> {
            directory = homeDirectory;
            setLastDirectory(directory);
            updateFiles(true);
        });

        iconTable.defaults().height(60).growX().padTop(5).uniform();
        iconTable.add(home);
        iconTable.add(back);
        iconTable.add(forward);
        iconTable.add(up);

        var fileName = new Table();
        fileName.bottom().left().add(new Label("@filename"));
        fileName.add(field).height(40f).fillX().expandX().padLeft(10f);

        var buttons = new Table();
        buttons.defaults().growX().height(60);
        buttons.add(cancel);
        buttons.add(ok);

        content.top().left();
        content.add(iconTable).expandX().fillX();
        content.row();

        content.center().add(pane).colspan(3).grow();
        content.row();

        if (!open) {
            content.bottom().left().add(fileName).colspan(3).grow().padTop(-2).padBottom(2);
            content.row();
        }

        content.add(buttons).growX();

        cont.add(content).grow();
    }

    private void updateFileFieldStatus() {
        if (!open) {
            ok.setDisabled(field.getText().isBlank());
        } else {
            ok.setDisabled(!directory.child(field.getText()).exists() || directory.child(field.getText()).isDirectory());
        }
    }

    private Fi[] getFileNames() {
        var handles = directory.list(file -> !file.getName().startsWith("."));

        Arrays.sort(handles, (a, b) -> {
            if (a.isDirectory() && !b.isDirectory()) return -1;
            if (!a.isDirectory() && b.isDirectory()) return 1;
            return String.CASE_INSENSITIVE_ORDER.compare(a.name(), b.name());
        });

        return handles;
    }

    void updateFiles(boolean push) {
        if (push) stack.push(directory);
        navigation.setText(directory.toString());

        var layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
        layout.setText(Fonts.def, navigation.getText());

        if (layout.width < navigation.getWidth())
            navigation.setCursorPosition(0);
        else
            navigation.setCursorPosition(navigation.getText().length());

        Pools.free(layout);

        table.clearChildren();
        table.top().left();
        var names = getFileNames();

        var buttonUp = new TextButton(".." + directory.toString(), Styles.checkTextButtonStyle);
        buttonUp.clicked(() -> {
            directory = directory.parent();
            setLastDirectory(directory);
            updateFiles(true);
        });

        buttonUp.left().add(new Image(Icons.up)).padRight(4f).padLeft(4);
        buttonUp.getLabel().setAlignment(Align.left);
        buttonUp.getCells().reverse();

        table.add(buttonUp).align(Align.topLeft).fillX().expandX().height(50).pad(2).colspan(2);
        table.row();

        var group = new ButtonGroup<>();
        group.setMinCheckCount(0);

        for (var file : names) {
            if (!file.isDirectory() && !filter.get(file)) continue; //skip non-filtered files

            var filename = file.name();

            var button = new TextButton(filename.replace("[", "[["), Styles.checkTextButtonStyle);
            button.getLabel().setWrap(false);
            button.getLabel().setEllipsis(true);
            group.add(button);

            button.clicked(() -> {
                if (!file.isDirectory()) {
                    field.setText(filename);
                    updateFileFieldStatus();
                } else {
                    directory = directory.child(filename);
                    setLastDirectory(directory);
                    updateFiles(true);
                }
            });

            field.changed(() -> button.setChecked(filename.equals(field.getText())));

            var image = new Image(file.isDirectory() ? Icons.folder : Icons.file);

            button.add(image).padRight(4f).padLeft(4);
            button.getCells().reverse();
            table.top().left().add(button).align(Align.topLeft).fillX().expandX()
                    .height(50).pad(2).padTop(0).padBottom(0).colspan(2);
            
            button.getLabel().setAlignment(Align.left);
            table.row();
        }

        pane.setScrollY(0f);
        updateFileFieldStatus();

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

            setLastDirectory(directory = get(--index - 1));
            updateFiles(false);
        }

        public void forward() {
            if (noForward()) return;

            setLastDirectory(directory = get(index++));
            updateFiles(false);
        }

        public boolean noForward() {
            return index >= size;
        }

        public boolean noBack() {
            return index == 1 || index <= 0;
        }
    }
}