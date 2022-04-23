import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lines;

    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        lines = new ArrayList<>();
        Arrays.sort(points);
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        int size = points.length;
        for (int i = 0; i < size - 3; i++) {
            Point anchor = points[i];
            Arrays.sort(pointsCopy, anchor.slopeOrder());

//            for(Point p: pointsCopy) {
//                System.out.println(anchor +"->"+p.toString()+" :"+anchor.slopeTo(p));
//            }
//            System.out.println();

            ArrayList<Point> collinearPoints = new ArrayList<>();
            collinearPoints.add(anchor);
            Point prev = pointsCopy[0];
            double prevSlope = anchor.slopeTo(prev);
            collinearPoints.add(prev);
            for (int j = 1; j < size; j++) {
                Point cur = pointsCopy[j];
                double curSlope = anchor.slopeTo(cur);
                if (curSlope == prevSlope) {
                    collinearPoints.add(cur);
                } else {
                    if (collinearPoints.size() >= 4) {
                        Point[] arr = collinearPoints.toArray(new Point[0]);
                        Arrays.sort(arr);
                        if (arr[0].compareTo(anchor) >= 0) {
                            lines.add(new LineSegment(arr[0], arr[arr.length - 1]));
                        }
                    }
                    collinearPoints.clear();
                    collinearPoints.add(anchor);
                    prev = cur;
                    prevSlope = curSlope;
                    collinearPoints.add(prev);
                }
            }
            if (collinearPoints.size() >= 4) {
                Point[] arr = collinearPoints.toArray(new Point[0]);
                Arrays.sort(arr);
                if (arr[0].compareTo(anchor) >= 0) {
                    lines.add(new LineSegment(arr[0], arr[arr.length - 1]));
                }
            }
        }

    }

    public int numberOfSegments()        // the number of line segments
    {
        return lines.size();
    }

    public LineSegment[] segments()                // the line segments
    {
        return lines.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

    // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        StdDraw.setPenRadius(0.003);
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
