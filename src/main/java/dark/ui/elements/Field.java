package dark.ui.elements;

import arc.func.Cons;
import arc.func.Intc;
import arc.scene.ui.TextField;
import arc.scene.ui.TextField.TextFieldFilter;
import arc.scene.ui.TextField.TextFieldValidator;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import dark.ui.Drawables;

public class Field extends Table {

    public Field(String name) {
        table(line -> {
            line.add(name);
            line.update(() -> line.setBackground(field().isValid() ? Drawables.underline : Drawables.underline_red));
        });
    }

    public Field(String name, String def, Cons<String> listener) {
        this(name);
        field(def, listener).width(50f);
    }

    public Field(String name, int def, Intc listener) {
        this(name);
        field(String.valueOf(def), TextFieldFilter.digitsOnly, value -> listener.get(Strings.parseInt(value))).valid(Strings::canParseInt).width(50f);
    }

    public TextField field() {
        return (TextField) children.peek();
    }

    public void valid(TextFieldValidator validator) {
        field().setValidator(validator);
    }
}
