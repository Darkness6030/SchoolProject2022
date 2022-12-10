package dark.ui.dialogs;

import arc.files.Fi;
import arc.func.Cons;
import arc.graphics.g2d.GlyphLayout;
import arc.scene.event.Touchable;
import arc.scene.ui.*;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.pooling.Pools;
import dark.ui.Fonts;
import dark.ui.Icons;
import dark.ui.Styles;

import java.util.Arrays;

import static arc.Core.files;
import static arc.Core.settings;

public class FileChooser extends BaseDialog {
    public static final Fi homeDirectory = files.absolute(files.getExternalStoragePath());
    public static Fi lastDirectory = files.absolute(settings.getString("lastDirectory", homeDirectory.absolutePath()));

    public Fi directory = lastDirectory;
    public Table filesTable;
    public ScrollPane pane;
    public TextField navigation, fileField;
    public final String extension;
    public final Cons<Fi> result;
    public final boolean open;

    public FileChooser(String title, String extension, boolean open, Cons<Fi> result) {
        super(title);
        setFillParent(true);

        this.open = open;
        this.extension = extension;
        this.result = result;

        if (!lastDirectory.exists())
            directory = lastDirectory = homeDirectory;

        shown(() -> {
            cont.clear();
            setupWidgets();
        });

        closeOnBack();
    }

    public void setupWidgets() {
        cont.margin(-10);

        fileField = new TextField();
        fileField.setOnlyFontChars(false);

        if (!open) fileField.addInputDialog();
        else fileField.setDisabled(true);

        var ok = new TextButton(open ? "@load" : "@save");
        ok.clicked(() -> {
            result.get(directory.child(fileField.getText()));
            hide();
        });

        ok.setDisabled(() -> open ? !directory.child(fileField.getText()).exists() || directory.child(fileField.getText()).isDirectory() : fileField.getText().isBlank());

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

        updateFiles();

        var iconTable = new Table();
        iconTable.defaults().height(60).growX().padTop(5).uniform();

        iconTable.button(Icons.folder, () -> {
            directory = directory.parent();
            updateFiles();
        });

        iconTable.button(Icons.home, () -> {
            setLastDirectory(directory = homeDirectory);
            updateFiles();
        });

        var content = new Table();
        content.top().left();
        content.add(iconTable).expandX().fillX();
        content.row();

        content.center().add(pane).colspan(3).grow();
        content.row();

        if (!open) {
            content.bottom().left().table(table -> {
                table.bottom().left().label(() -> "@file.name");
                table.add(fileField).height(40f).fillX().expandX().padLeft(10f);
            }).colspan(3).grow().padTop(-2).padBottom(2);

            content.row();
        }

        buttons.defaults().growX().height(60);
        buttons.add(cancel);
        buttons.add(ok);

        content.add(buttons).growX();
        cont.add(content).grow();
    }

    public Fi[] getFileNames() {
        var handles = directory.list(file -> !file.getName().startsWith("."));

        Arrays.sort(handles, (a, b) -> {
            if (a.isDirectory() && !b.isDirectory()) return -1;
            if (!a.isDirectory() && b.isDirectory()) return 1;
            return String.CASE_INSENSITIVE_ORDER.compare(a.name(), b.name());
        });

        return handles;
    }

    public void updateFiles() {
        navigation.setText(directory.toString());

        var layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
        layout.setText(Fonts.def, navigation.getText());

        if (layout.width < navigation.getWidth()) {
            navigation.setCursorPosition(0);
        } else {
            navigation.setCursorPosition(navigation.getText().length());
        }

        Pools.free(layout);

        filesTable.clearChildren();
        filesTable.top().left();
        var names = getFileNames();

        var up = new TextButton(".." + directory.toString(), Styles.checkTextButtonStyle);
        up.clicked(() -> {
            directory = directory.parent();
            setLastDirectory(directory);
            updateFiles();
        });

        up.left().add(new Image(Icons.up)).padRight(4f).padLeft(4);
        up.getLabel().setAlignment(Align.left);
        up.getCells().reverse();

        filesTable.add(up).align(Align.topLeft).fillX().expandX().height(50).pad(2).colspan(2);
        filesTable.row();

        var group = new ButtonGroup<>();
        group.setMinCheckCount(0);

        for (var file : names) {
            if (!file.isDirectory() && !file.extEquals(extension)) continue;

            var button = new TextButton(file.name().replace("[", "[["), Styles.checkTextButtonStyle);
            button.clicked(() -> {
                if (!file.isDirectory()) {
                    fileField.setText(file.name());
                } else {
                    directory = directory.child(file.name());
                    setLastDirectory(directory);
                    updateFiles();
                }
            });

            button.getLabel().setWrap(false);
            button.getLabel().setEllipsis(true);
            button.getLabel().setAlignment(Align.left);

            group.add(button);

            fileField.changed(() -> button.setChecked(file.name().equals(fileField.getText())));

            var image = new Image(file.isDirectory() ? Icons.folder : Icons.file);

            button.add(image).padRight(4f).padLeft(4);
            button.getCells().reverse();

            filesTable.top().left().add(button).align(Align.topLeft).fillX().expandX()
                    .height(50).pad(2).padTop(0).padBottom(0).colspan(2).row();
        }

        pane.setScrollY(0f);

        if (open) fileField.clearText();
    }

    public static void setLastDirectory(Fi directory) {
        lastDirectory = directory;
        settings.put("lastDirectory", directory.absolutePath());
    }
}
