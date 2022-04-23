import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[] array;
    private final int hamming;
    private final int manhattan;
    private final int dimension;
    private final int zeroIndex;

    private int getRow(int index) {
        return index / dimension;
    }

    private int getCol(int index) {
        return index % dimension;
    }

    private int getIndex(int row, int col) {
        return row * dimension + col;
    }

    // exchange constructor
    private Board(Board orignal, int top, int left) {
        this.dimension = orignal.dimension;
        int[] array = orignal.array.clone();
        int zeroIndex = orignal.zeroIndex;
        // row1, col1 original 0 -> x dist+ zeroIndex
        // row2, col2 exchange x -> 0 dist- newIndex
        int row1 = getRow(zeroIndex), col1 = getCol(zeroIndex);
        int row2 = row1 + top, col2 = col1 + left;
        int newIndex = getIndex(row2, col2);
        int tileNumber = array[newIndex];
        int tileRow = getRow(tileNumber - 1);
        int tileCol = getCol(tileNumber - 1);
        this.manhattan = orignal.manhattan + (Math.abs(tileRow - row1) + Math.abs(tileCol - col1)) - (Math.abs(tileRow - row2) + Math.abs(tileCol - col2));
        this.hamming = orignal.hamming + (tileNumber - 1 == zeroIndex ? 0 : 1) - (tileNumber - 1 == newIndex ? 0 : 1);
        array[zeroIndex] = array[newIndex];
        array[newIndex] = 0;
        this.array = array;
        this.zeroIndex = newIndex;
    }

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException();
        int[] array = new int[tiles.length * tiles.length];
        this.dimension = tiles.length;
        int manhattan = 0;
        int hamming = 0;
        int zeroIndex = -1;

        // copy of tiles && calculate manhattan & hamming
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                int index = getIndex(row, col);
                array[index] = tiles[row][col];
                if (tiles[row][col] == 0) {
                    zeroIndex = index;
                    continue;
                }
                manhattan += (Math.abs(row - getRow(tiles[row][col] - 1)) + Math.abs(col - getCol(tiles[row][col] - 1)));
                hamming += (tiles[row][col] - index == 1) ? 0 : 1;
            }
        }

        this.zeroIndex = zeroIndex;
        if (zeroIndex == -1) throw new IllegalArgumentException();
        this.array = array;
        this.manhattan = manhattan;
        this.hamming = hamming;

    }

    // no copy
    private Board(int[] arr, int d) {
        if (arr == null) throw new IllegalArgumentException();

        this.dimension = d;
        int zeroIndex = -1;
        int manhattan = 0;
        int hamming = 0;

        for (int row = 0; row < d; row++) {
            for (int col = 0; col < d; col++) {
                int index = getIndex(row, col);
                if (arr[index] == 0) {
                    zeroIndex = index;
                    continue;
                }
                manhattan += (Math.abs(row - getRow(arr[index] - 1)) + Math.abs(col - getCol(arr[index] - 1)));
                hamming += (arr[index] - index == 1) ? 0 : 1;
            }
        }

        this.array = arr;
        this.manhattan = manhattan;
        this.hamming = hamming;
        this.zeroIndex = zeroIndex;

        if (zeroIndex == -1) throw new IllegalArgumentException();
    }

    // string representation of this board
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(dimension).append("\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension - 1; j++) {
                ret.append(" ").append(array[getIndex(i, j)]);
            }
            ret.append(" ").append(array[getIndex(i, dimension - 1)]).append('\n');
        }
        return ret.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan == 0 && hamming == 0;
    }


    // does this board equal y?
    public boolean equals(Object y) {
        return Arrays.equals(array, ((Board) y).array);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return () -> {
            ArrayList<Board> ret = new ArrayList<>();
            int row = getRow(zeroIndex), col = getCol(zeroIndex);
            for (int offset = -1; offset <= 1; offset += 2) {
                if (checkPosition(row, col + offset))
                    ret.add(new Board(this, 0, offset));
                if (checkPosition(row + offset, col))
                    ret.add(new Board(this, offset, 0));
            }
            return ret.iterator();
        };
    }

    private boolean checkPosition(int row, int col) {
        return row >= 0 && col >= 0 && row < dimension && col < dimension;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (array.length <= 1) return null;
        for (int index = 0; index < array.length; index++) {
            if (array[index] == 0) continue;
            int row = getRow(index);
            int col = getCol(index);
            int newIndex = -1;
            if (row > 0 && array[getIndex(row - 1, col)] != 0) {
                newIndex = getIndex(row - 1, col);
            } else if (col > 0 && array[getIndex(row, col - 1)] != 0) {
                newIndex = getIndex(row, col - 1);
            } else if (row < dimension - 1 && array[getIndex(row + 1, col)] != 0) {
                newIndex = getIndex(row + 1, col);
            } else if (col < dimension - 1 && array[getIndex(row, col + 1)] != 0) {
                newIndex = getIndex(row, col + 1);
            }
            if (newIndex == -1) continue;

            // exchange tiles
            int[] copy = array.clone();
            int t = copy[newIndex];
            copy[newIndex] = copy[index];
            copy[index] = t;
            return new Board(copy, dimension);
        }
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        System.out.println("Twin:");
        System.out.println(initial.twin());
        System.out.println();
        System.out.println("Original:");
        System.out.println(initial);
        System.out.println("manhattan:" + initial.manhattan());
        System.out.println("hamming:" + initial.hamming());
        System.out.println("Neighborhood:");
        for (Board nb : initial.neighbors()) {
            System.out.println(nb);
            System.out.println("manhattan:" + nb.manhattan());
            System.out.println("hamming:" + nb.hamming());
            System.out.println();
        }
        ;
    }

}
