package byow.Core.Assets.WorldSaving;

import byow.Core.Display.Source;
import byow.Core.Input.StringSource;
import edu.princeton.cs.algs4.In;

public class ReadFile {

    public final static String filename = "previous_save.txt";
    private Source toBeCopied;

    public ReadFile() {
        In in = new In(filename);
        String moves = in.readAll();
        StringSource state = new StringSource(moves);
        toBeCopied = new Source(state);

    }

    public Source sourceToBeCopied() {
        return toBeCopied;
    }
}
