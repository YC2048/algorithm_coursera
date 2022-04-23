import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> set;
    private int size;

    public PointSET()                               // construct an empty set of points
    {
        set = new TreeSet<>();
        size = 0;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return set.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return size;
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException("Insert null point!\n");
        set.add(p);
        size++;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException("Set cannot contain null point!\n");
        return set.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D p : set) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException("Null Rectangle has no range\n");
        ArrayList<Point2D> list = new ArrayList<>();
        for (Point2D p : set) {
            if (rect.contains(p)) list.add(p);
        }
        return list;
    }


    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException("Null point has no nearest neighbor!\n");
        if (set.isEmpty()) return null;
        Point2D neartest = null;
        for (Point2D cur : set) {
            if (neartest == null || p.distanceTo(neartest) > p.distanceTo(cur)) neartest = cur;
        }
        return neartest;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {

    }
}
