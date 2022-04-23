import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> lines;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        // Throw an IllegalArgumentException if the argument to the constructor is null
        if (points == null) {
            throw new IllegalArgumentException();
        }
        Arrays.sort(points);
        lines = new ArrayList<>();
        int len = points.length;
        for (int i = 0; i < len - 3; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            for (int j = i+1; j < len - 2; j++) {
                if (points[j] == null) {
                    throw new IllegalArgumentException();
                }
                double slope1 = points[i].slopeTo(points[j]);
                for (int k = j+1; k < len - 1; k++) {
                    if (points[k] == null) {
                        throw new IllegalArgumentException();
                    }
                    double slope2 = points[j].slopeTo(points[k]);
                    if (slope1 != slope2) continue;
                    for (int l = k+1; l < len; l++) {
                        if (points[l] == null) {
                            throw new IllegalArgumentException();
                        }
                        double slope3 = points[k].slopeTo(points[l]);
                        if (slope2 == slope3) {
                            Point[] t = {points[i], points[j], points[k], points[l]};
                            Arrays.sort(t);
                            lines.add(new LineSegment(t[0], t[3]));
                        }
                    }
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
}
