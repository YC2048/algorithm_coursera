import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

// Move-to-front encoding.
// Given a text file in which sequences of the same character occur near each other many times,
// convert it into a text file in which certain characters appear much more frequently than others.
public class MoveToFront {
    // make a last f
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        var ascii = new LinkedList<Character>();
        for (int i = 0; i < 256; i++)
            ascii.add((char) i);
        while (!BinaryStdIn.isEmpty()) {
            // using type byte -> ascii.remove(int index) -> E
            // using type Byte -> ascii.remove(Object obj) -> boolean
            int index = ascii.indexOf(BinaryStdIn.readChar());
            ascii.addFirst(ascii.remove(index));
            BinaryStdOut.write(index, 8); // (byte) index
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> ascii = new LinkedList<>();
        for (int i = 0; i < 256; i++)
            ascii.add((char) i);
        while (!BinaryStdIn.isEmpty()) {
            // convert Byte to unsigned int
            int index = BinaryStdIn.readInt(8);
            ascii.addFirst(ascii.remove(index));
            BinaryStdOut.write(ascii.getFirst(), 8);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length < 1 ) throw new IllegalArgumentException();
        if ("-".equals(args[0])) encode();
        if ("+".equals(args[0])) decode();
    }

}
