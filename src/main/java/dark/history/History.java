package dark.history;

import arc.struct.Seq;

public class History extends Seq<Operation> {

    public int index;

    @Override
    public Seq<Operation> add(Operation op) {
        if (index != size) truncate(index); // new operation added not to the end of the story

        index++;
        return add(op);
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
