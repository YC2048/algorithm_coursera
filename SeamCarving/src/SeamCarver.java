import edu.princeton.cs.algs4.Picture;

// The width(), height(), and energy() methods should take constant time in the worst case.
// All other methods should run in time at most proportional to width × height in the worst case.
// For faster performance, do not construct explicit DirectedEdge and EdgeWeightedDigraph objects.
public class SeamCarver {
    private int[] pixels;
    private int height;
    private int width;
//    private boolean isTransposed;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.height = picture.height();
        this.width = picture.width();
        var pixels = new int[height * width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pixels[row * width + col] = picture.getRGB(col, row);
            }
        }
        this.pixels = pixels;
//        this.isTransposed = false;
    }


    // O(H * W)
    // current picture
    public Picture picture() {
        var picture = new Picture(width, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                picture.setRGB(col, row, pixels[row * width + col]);
            }
        }
        return picture;
    }

    // O(1)
    // width of current picture
    public int width() {
        return width;
    }

    // O(1)
    // height of current picture
    public int height() {
        return height;
    }

    //O(1)
    // energy of pixel at column x and row y
    public double energy(int col, int row) {
        checkCoordinate(row, col);
        if (col == 0 || row == 0 || col == width() - 1 || row == height() - 1)
            return 1000;
        double energy = 0;
        if (col > 0 && col < width - 1)
            energy += gradient(pixels[row * width + col + 1], pixels[row * width + col - 1]);
        if (row > 0 && row < height - 1)
            energy += gradient(pixels[(row + 1) * width + col], pixels[(row - 1) * width + col]);
        return Math.sqrt(energy);
    }


    private double gradient(int rgbA, int rgbB) {
        double energy = 0;
        energy += Math.pow(((rgbA >> 16) & 0xff) - ((rgbB >> 16) & 0xff), 2);
        energy += Math.pow(((rgbA >> 8) & 0xff) - ((rgbB >> 8) & 0xff), 2);
        energy += Math.pow((rgbA & 0xff) - (rgbB & 0xff), 2);
        return energy;
    }

    private void checkCoordinate(int row, int col) {
        if (row < 0) throw new IllegalArgumentException();
        if (col < 0) throw new IllegalArgumentException();
        if (row >= height()) throw new IllegalArgumentException();
        if (col >= width()) throw new IllegalArgumentException();
    }

    private static class Edge {
        final int offset;
        Edge prev;
        double cost;

        Edge(Edge prev, int offset, double cost) {
            this.prev = prev;
            this.offset = offset;
            this.cost = cost;
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findSeam(toEnergyMatrix(true));
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(toEnergyMatrix(false));
    }

    // default: vertical
    // horizontal need transposition
    private int[] findSeam(double[][] energyMatrix) {
        int height = energyMatrix.length;
        int width = energyMatrix[0].length;
        Edge[] prevRow = new Edge[width];
        for (int col = 0; col < width; col++) {
            prevRow[col] = new Edge(null, col, energyMatrix[0][col]);
        }
        int lastIndex = 0;
        for (int row = 1; row < height; row++) {
            Edge[] curRow = new Edge[width];
            for (int col = 0; col < width; col++) {
                curRow[col] = new Edge(prevRow[col], col, prevRow[col].cost);
                if (col > 0 && prevRow[col - 1].cost < curRow[col].cost) {
                    curRow[col].prev = prevRow[col - 1];
                    curRow[col].cost = prevRow[col - 1].cost;
                }
                if (col < width - 1 && prevRow[col + 1].cost < curRow[col].cost) {
                    curRow[col].prev = prevRow[col + 1];
                    curRow[col].cost = prevRow[col + 1].cost;
                }
                curRow[col].cost += energyMatrix[row][col];
                if (row == height - 1 && curRow[col].cost < curRow[lastIndex].cost) {
                    lastIndex = col;
                }
            }
            prevRow = curRow;
        }
        int[] ret = new int[height];
        Edge last = prevRow[lastIndex];
        for (int i = height - 1; i >= 0; i--) {
            ret[i] = last.offset;
            last = last.prev;
        }
        return ret;
    }

    // TODO expansion of transposition of 1d-Array-stored Matrix
    private double[][] toEnergyMatrix(boolean isTransposed) {
        if (!isTransposed) {
            double[][] energyMatrix = new double[height][width];
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    energyMatrix[row][col] = energy(col, row);
                }
            }
            return energyMatrix;
        } else {
            double[][] energyMatrix = new double[width()][height()];
            for (int col = 0; col < width(); col++) {
                for (int row = 0; row < height(); row++) {
                    energyMatrix[col][row] = energy(col, row);
                }
            }
            return energyMatrix;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
       if (seam == null) throw new IllegalArgumentException();
       if (height <= 1) throw new IllegalArgumentException();
       if (seam.length != width) throw new IllegalArgumentException();
       int[] pixels = new int[(height - 1) * width];
        for (int col = 0; col < width; col++) {
            if (seam[col] >= height || seam[col] < 0 || (col >= 1 && Math.abs(seam[col] - seam[col - 1]) > 1) ) throw new IllegalArgumentException();
            for (int row = 0; row < seam[col]; row++) {
                pixels[col + row * width] = this.pixels[col + row * width];
            }
            for (int row = seam[col]; row < height - 1; row++) {
                pixels[col + row * width] = this.pixels[col + (row + 1) * width];
            }
        }
        this.pixels = pixels;
        height--;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (width <= 1) throw new IllegalArgumentException();
        if (seam.length != height) throw new IllegalArgumentException();
        int[] pixels = new int[height * (width - 1)];
        for (int row = 0; row < height; row++) {
            if (seam[row] >= width || seam[row] < 0 || (row >= 1 && Math.abs(seam[row] - seam[row - 1]) > 1) ) throw new IllegalArgumentException();

            System.arraycopy(this.pixels, row * width, pixels, row * (width - 1), seam[row]);
            System.arraycopy(this.pixels, row * width + seam[row] + 1, pixels, row * (width - 1) + seam[row], width - seam[row] - 1);
        }
        this.pixels = pixels;
        width--;
    }


//    private int[] removeSeam(int[] seam, boolean isTransposed) {
//        int height = !isTransposed ? this.height : this.width;
//        int width = !isTransposed ? this.width : this.height;
//
//        return pixels;
//    }

//    // TODO:
//    //  - in-place transposition of a matrix
//    void transpose(int[] matrix, int height, int width) {
//
//    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        picture.show();
        SeamCarver sc = new SeamCarver(picture);
        SCUtility.showEnergy(sc);
    }

}
