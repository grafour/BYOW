package byow.Core.Assets.WorldSaving;
import edu.princeton.cs.algs4.Out;

public class SaveFile {

    public final static String filename = "previous_save.txt";

    public static void save(String stringToSave) {
        Out out = new Out(filename);
        out.println(stringToSave);
    }

}
