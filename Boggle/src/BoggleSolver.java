import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private final Trie trie = new Trie();

    public BoggleSolver(String[] dictionary) {
        var dt = dictionary.clone();
        // filter Words with Q while without Qu
        Arrays.stream(dt).
                filter(word -> word.length() > 2 && (!word.contains("Q") || word.contains("QU") && word.contains("Q"))).
                forEach(word -> this.trie.insert(word.replace("QU", "Q")));
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard b) {
        char[][] board = new char[b.rows()][b.cols()];
        for (int r = 0; r < b.rows(); r++) {
            for (int c = 0; c < b.cols(); c++) {
                board[r][c] = b.getLetter(r, c);
            }
        }
        var set = new TreeSet<String>();
        for (int r = 0; r < b.rows(); r++) {
            for (int c = 0; c < b.cols(); c++) {
                if (trie.getRoot().getNext(board[r][c]) == null) continue;
                set.addAll(backTrack(board, r, c, trie.getRoot()));
            }
        }
        return set;
    }


    private Set<String> backTrack(char[][] board, int row, int col, Trie.Node cur) {
        var ret = new HashSet<String>();
        // mark cur && board visited
        char ch = board[row][col];
        cur = cur.getNext(board[row][col]);
        if (cur.word != null)
            ret.add(cur.word.replace("Q", "QU"));
        board[row][col] = 0;
        // search neighbors
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {

                // check legality to access the board
                if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) continue;
                // check board[r][c] is a character for cur.getNext
                // i.e. board[r][c] >= 'A' && board[r][c] <= 'Z';
                if (board[r][c] == 0) continue;
                // check if the node is accessible
                // the cur char chain does not match the board chain
                if (cur.getNext(board[r][c]) == null) continue;

                ret.addAll(backTrack(board, r, c, cur));
            }
        }
        board[row][col] = ch;
        return ret;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!trie.contain(word.replace("QU", "Q")) || word.length() < 3) return 0;
        if (word.length() <= 4) return 1;
        if (word.length() <= 5) return 2;
        if (word.length() <= 6) return 3;
        if (word.length() <= 7) return 5;
        return 11;
    }

    private static class Trie {
        private static class Node {
            Node[] next = new Node[26];
            String word = null;

            public Node getNext(char ch) {
                if (ch < 'A' || ch > 'Z') throw new IllegalArgumentException(String.valueOf(ch));
                return this.next[ch - 'A'];
            }
        }

        private final Node root = new Node();

        public void insert(String word) {
            var cur = root;
            for (int i = 0; i < word.length(); i++) {
                if (cur.getNext(word.charAt(i)) == null)
                    cur.next[word.charAt(i) - 'A'] = new Node();
                cur = cur.getNext(word.charAt(i));
            }
            if (cur.word == null)
                cur.word = word;
        }

        public boolean contain(String word) {
            var cur = root;
            for (int i = 0; i < word.length(); i++) {
                if (cur.getNext(word.charAt(i)) == null) return false;
                cur = cur.getNext(word.charAt(i));
            }
            return cur.word != null;
        }

        public Node getRoot() {
            return this.root;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
