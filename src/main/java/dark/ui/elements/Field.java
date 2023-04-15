package dark.ui.elements;

import arc.func.*;
import arc.scene.ui.*;
import arc.scene.ui.TextField.*;
import arc.scene.ui.layout.Table;
import arc.util.*;
import dark.ui.Drawables;

public class Field extends Table {

    public Label label;
    public TextField field;

    public Field(String name, float width, String def, Cons<String> listener) {
        left();
        table(line -> {
            label = line.add(name).get();
            label.clicked(() -> {
                if (field.hasKeyboard()) return;

                field.requestKeyboard();
                field.setCursorPosition(field.getText().length());
            });

            line.update(() -> line.setBackground(valid() ? focused() ? Drawables.field_focused : Drawables.field_main : Drawables.field_invalid));
        });

        field = field(def, listener).width(width).get();
        field.setAlignment(Align.right);
    }

    public Field(String name, float width, float def, Floatc listener) {
        this(name, width, String.valueOf(def), value -> listener.get(Strings.parseFloat(value)));
        valid(Strings::canParseFloat);
        floatsOnly();
    }

    public Field(String name, float width, int def, Intc listener) {
        this(name, width, String.valueOf(def), value -> listener.get(Strings.parseInt(value)));
        valid(Strings::canParseInt);
        digitsOnly();
    }

    public Field(String name, float width, int def, int length, int min, int max, Intc listener) {
        this(name, width, def, listener);
        maxTextLength(length);
        valid(value -> {
            int number = Strings.parseInt(value);
            return number >= min && number <= max;
        });
    }

    public boolean valid() {
        return field.isValid();
    }

    public boolean focused() {
        return field.hasKeyboard();
    }

    public void onClicked(Runnable runnable) {
        label.clicked(runnable);
        field.clicked(runnable);
    }

    // region validation

    public void maxTextLength(int maxLength) {
        field.setMaxLength(maxLength);
    }

    public void valid(TextFieldValidator validator) {
        field.setValidator(validator);
    }

    // endregion
    // region filters

    public void filter(TextFieldFilter filter) {
        field.setFilter(filter);
    }

    public void digitsOnly() {
        filter(TextFieldFilter.digitsOnly);
    }

    public void floatsOnly() {
        filter(TextFieldFilter.floatsOnly);
    }

    // endregion
    // region text

    public String getText() {
        return field.getText();
    }

    public void setText(String text) {
        field.setText(text);
    }

    public void setTextSafe(String text) {
        if (!focused()) setText(text);
    }

    public void setCursorPosition(int position) {
        field.setCursorPosition(position);
    }

    // endregion
}