package dark.ui.elements;

import arc.func.*;
import arc.scene.ui.TextField;
import arc.scene.ui.TextField.TextFieldFilter;
import arc.scene.ui.TextField.TextFieldValidator;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Strings;
import dark.ui.Drawables;

public class Field extends Table {

    private Field(String name) {
        left();
        table(line -> {
            line.add(name);
            line.update(() -> line.setBackground(field().isValid() ? Drawables.underline : Drawables.underline_red));
        });
    }

    public Field(String name, float width, String def, Cons<String> listener) {
        this(name);
        field(def, listener).width(width).get().setAlignment(Align.right);
    }

    public Field(String name, float width, float def, Floatc listener) {
        this(name);
        field(String.valueOf(def), value -> listener.get(Strings.parseFloat(value))).valid(Strings::canParseFloat)
                .width(width).get().setAlignment(Align.right);
        floatsOnly();
    }

    public Field(String name, float width, int def, Intc listener) {
        this(name);
        field(String.valueOf(def), value -> listener.get(Strings.parseInt(value))).valid(Strings::canParseInt)
                .width(width).get().setAlignment(Align.right);
        digitsOnly();
    }

    public TextField field() {
        return (TextField) children.peek();
    }

    public void maxTextLength(int maxLength) {
        field().setMaxLength(maxLength);
    }

    public void valid(TextFieldValidator validator) {
        field().setValidator(validator);
    }

    // region filters

    public void filter(TextFieldFilter filter) {
        field().setFilter(filter);
    }

    public void digitsOnly() {
        filter(TextFieldFilter.digitsOnly);
    }

    public void floatsOnly() {
        filter(TextFieldFilter.floatsOnly);
    }

    // endregion
    // region text

    public void setText(String text) {
        field().setText(text);
    }

    public void setTextSafe(String text) {
        if (!field().hasKeyboard()) setText(text);
    }

    // endregion
}