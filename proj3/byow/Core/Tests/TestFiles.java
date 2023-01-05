package byow.Core.Tests;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

public class TestFiles {

    public final static String filename = "example_file.txt";

    public static void main(String[] args) {

        System.out.println("Writing to " + filename);
        Out out = new Out(filename);
        out.println("This text will appear in the file!");
        out.println("Your lucky number is " + System.currentTimeMillis() % 100);

        System.out.println("Reading from " + filename + ". Contents are:");
        In in = new In(filename);
        String s = in.readAll();
        System.out.println(s);

    }
}
