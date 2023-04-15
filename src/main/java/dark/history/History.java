package dark.history;

import arc.struct.Seq;

import static dark.Main.ui;

public class History extends Seq<Operation> {

    public static final int max = 64;
    public int index;

    public void push(Operation op) {
        if (index != size) truncate(index); // новая операция добавлена не в конец массива истории
        add(op);

        if (size > max) remove(0);
        else index++;

        ui.hudFragment.updateHistory();
    }

    @Override
    public Seq<Operation> clear() {
        super.clear();
        index = 0;

        ui.hudFragment.updateHistory();
        return this;
    }

    public Operation selected() {
        return index == 0 ? null : get(index - 1);
    }

    public void undo() {
        get(--index).undo();
    }

    public void redo() {
        get(index++).redo();
    }

    public boolean hasUndo() {
        return index >= 1;
    }

    public boolean hasRedo() {
        return index < size;
    }

    public void undoTo(int opi) {
        while (index != opi + 1) undo();
    }

    public void redoTo(int opi) {
        while (index != opi + 1) redo();
    }

    public void moveTo(Operation op) {
        int opi = indexOf(op);
        if (opi == -1) return; // I'm sorry, what?

        if (opi >= index)
            redoTo(opi);
        else
            undoTo(opi);
    }
}