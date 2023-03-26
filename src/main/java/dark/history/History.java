package dark.history;

import arc.struct.Seq;

import static dark.Main.*;

public class History extends Seq<Operation> {

    public int index = -1;

    public void push(Operation op) {
        if (index != size - 1) truncate(index + 1); // new operation added not to the end of the story

        add(op);
        index++;

        ui.hudFragment.updateHistory();
    }

    public void undo() {
        get(index).undo();
        index--;
    }

    public void redo() {
        index++;
        get(index).redo();
    }

    public boolean hasUndo() {
        return index >= 0;
    }

    public boolean hasRedo() {
        return index < size - 1;
    }
}
