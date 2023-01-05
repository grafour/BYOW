package byow.Core.Input;

import byow.Core.Input.InputSource;

public class StringSource implements InputSource {

    private String str;
    private int index;

    public StringSource(String s) {
        this.str = s;
        this.index = 0;
    }

    public char getNextKey() {
        char c = Character.toUpperCase(str.charAt(index));
        index += 1;
        return c;
    }

    public boolean hasNextKey() {
        return index < str.length();
    }
}
