import java.util.Arrays;

public class CircularSuffixArray {
    private final Integer[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        index = sort(s);
    }

    private Integer[] sort(String s) {
        // cannot infer type error for lambda with int type
        Integer[] ret = new Integer[s.length()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = i;
        Arrays.sort(ret, (i1, i2) -> {
            Integer index1 = i1;
            Integer index2 = i2;
            // find the first different character
            while (s.charAt(index1) == s.charAt(index2)) {
                index1 = (index1 + 1) % s.length();
                index2 = (index2 + 1) % s.length();
                if (index1.equals(i1) || index2.equals(i2)) return 0;
            }
            return s.charAt(index1) < s.charAt(index2) ? -1 : 1;
        });
        return ret;
    }

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= index.length) throw new IllegalArgumentException();
        return index[i];
    }

}
