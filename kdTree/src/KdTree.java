import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class KdTree {
    private static class Node {
        private final boolean isOddLevel;
        private final Point2D data;
        private final RectHV rect;
        public Node less;
        public Node more;

        public Node(Point2D data) {
            if (data == null) throw new IllegalArgumentException("Null Node\n");
            this.data = data;
            this.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            this.less = null;
            this.more = null;
            this.isOddLevel = false;
        }

        public Node(Point2D data, boolean isOddLevel, RectHV rect) {
//            if (data == null) throw new IllegalArgumentException("Null Point\n");
            this.data = data;
            this.less = null;
            this.more = null;
            this.isOddLevel = isOddLevel;
            this.rect = rect;
        }

        // 0  eq
        // 1  point locate in the right p - this > 0
        // -1 point locate on the left p - this < 0
        public int compareTo(Point2D point) {
            if (!isOddLevel) {
                // per x in even levels
                return Double.compare(point.x(), this.data.x());
            } else {
                // per y in odd levels
                return Double.compare(point.y(), this.data.y());
            }
        }

        public int compareTo(RectHV rect) {
            int d1, d2;
            if (!isOddLevel) {
                d1 = Double.compare(rect.xmin(), this.data.x());
                d2 = Double.compare(rect.xmax(), this.data.x());
            } else {
                d1 = Double.compare(rect.ymin(), this.data.y());
                d2 = Double.compare(rect.ymax(), this.data.y());
            }
            // check the left subtree
            if (d1 > 0) return -1;
            // check the right subtree
            if (d2 < 0) return 1;
            // check the current subtree
            return 0;
        }

        public void createChild(Point2D data) {
            if (data == null) throw new IllegalArgumentException("Null Node\n");
            // equals go the right subtree as specified.
            if (this.compareTo(data) < 0) {
                if (this.less == null) {
                    RectHV rect;
                    if (!isOddLevel) {
                        rect = new RectHV(this.rect.xmin(), this.rect.ymin(), this.data.x(), this.rect.ymax());
                    } else {
                        rect = new RectHV(this.rect.xmin(), this.rect.ymin(), this.rect.xmax(), this.data.y());
                    }
                    this.less = new Node(data, !isOddLevel, rect);
                    return;
                }
                this.less.createChild(data);
            } else {
                if (this.more == null) {
                    RectHV rect = !isOddLevel ? (new RectHV(this.data.x(), this.rect.ymin(), this.rect.xmax(), this.rect.ymax())) : (new RectHV(this.rect.xmin(), this.data.y(), this.rect.xmax(), this.rect.ymax()));
                    this.more = new Node(data, !isOddLevel, rect);
                    return;
                }
                this.more.createChild(data);
            }
        }

        public void draw() {
            double fromX, fromY, toX, toY;
            if (!isOddLevel) {
                fromY = this.rect.ymin();
                toY = this.rect.ymax();
                fromX = toX = this.data.x();
            } else {
                fromX = this.rect.xmin();
                toX = this.rect.xmax();
                fromY = toY = this.data.y();
            }
            this.data.draw();
            StdDraw.setPenColor((!isOddLevel) ? StdDraw.RED : StdDraw.BLUE);
            StdDraw.line(fromX, fromY, toX, toY);
//            rect.draw();
        }
    }

    private Node root;
    private int size;

    public KdTree()                               // construct an empty set of points
    {
        root = null;
        size = 0;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return null == root;
    }

    public int size()                         // number of points in the set
    {
        return size;
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (root == null) {
            root = new Node(p);
            size = 1;
            return;
        }
        root.createChild(p);
        size++;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException("Null Contains\n");
        Node cur = root;
        while (cur != null) {
            if (cur.data.equals(p)) return true;
            int compare = cur.compareTo(p);
            if (compare < 0) cur = cur.less;
            else cur = cur.more;
        }
        return false;
    }

    public void draw()                         // draw all points to standard draw
    {
        if (root == null) return;
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node p = q.poll();
            if (p.less != null) q.add(p.less);
            if (p.more != null) q.add(p.more);
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException("Null Rectangle\n");
        if (root == null) return null;
        return range(rect, root);
    }

    private List<Point2D> range(RectHV rect, Node cur) {
        if (root == null) return null;
        LinkedList<Point2D> ret = new LinkedList<>();
        int compare = cur.compareTo(rect);
        if (compare == 0 && rect.contains(cur.data)) ret.add(cur.data);
        if (compare >= 0 && cur.less != null) {
            List<Point2D> left = range(rect, cur.less);
            if (left != null) ret.addAll(left);
        }
        if (compare <= 0 && cur.more != null) {
            List<Point2D> right = range(rect, cur.more);
            if (right != null) ret.addAll(right);
        }
        return ret.isEmpty() ? null : ret;
    }


    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException("Null Nearest\n");
        if (this.isEmpty()) return null;
        return nearest(p, root);
    }

    private Point2D nearest(Point2D p, Node cur) {
        Point2D ret = cur.data;
        double dist = p.distanceSquaredTo(ret);

        if (cur.less != null && cur.less.rect.contains(p)) {
            Point2D child = nearest(p, cur.less);
            double d = p.distanceSquaredTo(child);
            if (d < dist) {
                ret = child;
                dist = d;
            }
            if (cur.more != null && cur.more.rect.distanceSquaredTo(p) < dist) {
                child = nearest(p, cur.more);
                d = p.distanceSquaredTo(child);
                if (d < dist) {
                    ret = child;
                    dist = d;
                }
            }
        } else if (cur.more != null && cur.more.rect.contains(p)) {
            Point2D child = nearest(p, cur.more);
            double d = p.distanceSquaredTo(child);
            if (d < dist) {
                ret = child;
                dist = d;
            }
            if (cur.less != null && cur.less.rect.distanceSquaredTo(p) < dist) {
                child = nearest(p, cur.less);
                d = p.distanceSquaredTo(child);
                if (d < dist) {
                    ret = child;
                    dist = d;
                }
            }
        } else {
            if (cur.less != null && cur.less.rect.distanceSquaredTo(p) < dist) {
                Point2D child = nearest(p, cur.less);
                double d = p.distanceSquaredTo(child);
                if (d < dist) {
                    ret = child;
                    dist = d;
                }
            }
            if (cur.more != null && cur.more.rect.distanceSquaredTo(p) < dist) {
                Point2D child = nearest(p, cur.more);
                double d = p.distanceSquaredTo(child);
                if (d < dist) {
                    ret = child;
                    dist = d;
                }
            }
        }
        return ret;
    }
}
