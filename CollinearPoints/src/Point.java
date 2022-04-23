/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        double deltaY = that.y - this.y;
        double deltaX = that.x - this.x;
        if (deltaX == 0) {
            if (deltaY == 0) {
                return Double.NEGATIVE_INFINITY;
            } else {
                return Double.POSITIVE_INFINITY;
            }
        }
        return deltaY / deltaX;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        // delta y > 0 --> positive number
        // delta y < 0 --> negative number
        // delta y == 0 -->
        //      delta x == 0 --> zero
        //      delta x > 0 --> positive
        //      delta x < 0 --> negative
        if (this.y == that.y) {
            return this.x - that.x;
        } else return this.y - that.y;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return (o1, o2) -> {
            if (o1 == null || o2 == null) {
                throw new IllegalArgumentException();
            }
//            System.out.println("("+x+","+y+")->("+o1.x+","+o1.y+")"+" slope:"+slopeTo(o1));
//            System.out.println("("+x+","+y+")->("+o2.x+","+o2.y+")"+" slope:"+slopeTo(o2));
//            System.out.println("real:"+(slopeTo(o1) - slopeTo(o2)));
//            System.out.println("return:"+Double.compare(slopeTo(o1),slopeTo(o2)));
            double slope1 = slopeTo(o1);
            double slope2 = slopeTo(o2);
            return Double.compare(slope1,slope2);
        };
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
//        Point pt1 = new Point(1, 10);
//        Point pt2 = new Point(2, 10);
//        Point pt3 = new Point(3, 9);
//        Point pt4 = new Point(4, 8);
//        Point pt5 = new Point(100, 9);
//        Point pt6 = new Point(100, 7);
//        System.out.println(pt1.slopeTo(pt2));
//        System.out.println(pt2.slopeTo(pt1));
//        System.out.println(pt4.compareTo(pt5));
//        System.out.println(pt4.compareTo(pt6));
//        System.out.println(pt3.slopeTo(pt6));
//        pt1.drawTo(pt2);
//        pt1.draw();
//        pt2.drawTo(pt6);
//        StdDraw.show();
    }
}
