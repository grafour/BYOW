package byow.Core.Input;

import edu.princeton.cs.algs4.StdDraw;

public class KeyboardSource implements InputSource{

    public KeyboardSource() {
    }

    public char getNextKey() {
        if (StdDraw.hasNextKeyTyped()) {
            char c = Character.toUpperCase(StdDraw.nextKeyTyped());
            return c;
        }
        return '\0';
    }

    public boolean hasNextKey() {
        return true;
    }


}
