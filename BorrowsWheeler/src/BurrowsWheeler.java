import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

// Burrows–Wheeler transform.
// Given a typical English text file,
// transform it into a text file in which sequences of the same character occur near each other many times.
public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        var str = BinaryStdIn.readString();
        var csa = new CircularSuffixArray(str);
        // output the first
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        // output the last column
        for (int i = 0; i < csa.length(); i++) {
            int index = ((csa.index(i) + csa.length() - 1)) % csa.length();
            BinaryStdOut.write(str.charAt(index));
        }
        BinaryStdOut.close();
    }

    // if the t[] is "A R D ! R C A A A A B B"
    // first col:    "! A A A A A B B C D R R"
    // when rotate to previous one
    //      <<<<<< move to front <<<<<
    // 0    ! ? ? ? ? ? ? ? ? ? ? A        3    // the first letter move to front, '!' come to the tail, track the row that ends with '!'
    // 1    A ? ? ? ? ? ? ? ? ? ? R             // as it is indicated from first, it was the first col letter of "first" row sorted sequence -- 'A'
    // 2    A ? ? ? ? ? ? ? ? ? ? D
    //*3    A ? ? ? ? ? ? ? ? ? ? !             // A move to the end, while you may find row 0, 6, 7, 8, 9 end with 'A'.
    // 4    A ? ? ? ? ? ? ? ? ? ? R             // However, the next[i] accords with the order of A
    // 5    A ? ? ? ? ? ? ? ? ? ? C             // e.g. row 1 head 'A' pairs the first tail 'A' of row 0
    // 6    B ? ? ? ? ? ? ? ? ? ? A             //      row 2 head 'A' pairs the second tail 'A' of row 6
    // 7    B ? ? ? ? ? ? ? ? ? ? A             // Therefore row 3 head 'A' pairs row 7
    // 8    C ? ? ? ? ? ? ? ? ? ? A        5    // it will be much easier to find the row that ends with 'C'
    // 9    D ? ? ? ? ? ? ? ? ? ? A        2    // same as above
    //10    R ? ? ? ? ? ? ? ? ? ? B
    //11    R ? ? ? ? ? ? ? ? ? ? B

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        var lastCol = BinaryStdIn.readString();
        var csa = new CircularSuffixArray(lastCol);

//        int[] next = new int[lastCol.length()];
        // O(n * n) but require O(n)
//        int[] ascii = new int[256];
//        for (int i = 0; i < lastCol.length(); i++) {
//            var firstLetter = lastCol.charAt(csa.index(i));
//            for (int j = ascii[firstLetter]; j < lastCol.length(); j++) {
//                if (lastCol.charAt(j) == firstLetter) {
//                    next[i] = j;
//                    ascii[firstLetter] = j + 1; // next time start from the larger index
//                    break;
//                }
//            }
//        }


        // calculate prefix sum
        // As the first col is a sorted array,
        // it will be easy to get the tail when we know its index
        // e.g. 'A' index is 1 as it only has one prefix '!'
        //      when we know the suffix is 'A' we just search the ordered index of A
        //      and find the row with new suffix
        // in order to deal with duplicate, we add 1 if visited once

        int[] ascii = new int[256 + 1];
        for (int i = 0; i < lastCol.length(); i++) {
            ascii[lastCol.charAt(i) + 1]++;
        }
        for (int i = 0; i < 256; i++) {
            ascii[i + 1] += ascii[i];
        }

        int[] next = new int[lastCol.length()];
        for (int i = 0; i < lastCol.length(); i++) {
            next[ascii[lastCol.charAt(i)]] = i;
            ascii[lastCol.charAt(i)]++;
        }

        for (int i = 0; i < lastCol.length(); i++) {
            char out = lastCol.charAt(csa.index(first));
            BinaryStdOut.write(out);
            first = next[first];
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length < 1) throw new IllegalArgumentException();
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }

}
