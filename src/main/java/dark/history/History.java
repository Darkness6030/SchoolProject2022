package dark.history;

import arc.struct.Seq;

import static dark.Main.*;

public class History extends Seq<Operation> {

    public int index;

    public void push(Operation op) {
        if (index != size) truncate(index); // new operation added not to the end of the story

        add(op);
        index++;

        ui.hudFragment.updateHistory();
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
}
