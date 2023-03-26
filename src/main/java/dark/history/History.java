package dark.history;

import arc.struct.Seq;

public class History extends Seq<Operation> {

    public int index;

    public void push(Operation op) {
        if (index != size) truncate(index); // new operation added not to the end of the story

        add(op);
        index++;
    }

    public void undo() {
        index--;
        get(index).undo();
    }

    public void redo() {
        get(index).redo();
        index++;
    }

    public boolean noUndo() {
        return index <= 1;
    }

    public boolean noRedo() {
        return index >= size;
    }
}
