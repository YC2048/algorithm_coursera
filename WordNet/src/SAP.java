import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.List;

public class SAP {
    private final Digraph digraph;

    private static class Pair {
        public final int left;
        public final int right;

        Pair(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.digraph = new Digraph(G);
    }

    // SOLVED:
    // level traverse search from each ends in shortest step
    // while one ends may search much more steps than the other's
    // in which the algorithm require search similar step from each side
    // REPLACE WITH BFS
    private Pair bfs(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(digraph, w);
        int vertex = -1, length = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            // BreadthFirstDirectedPath defaults Infinity
            if (bfdpV.distTo(i) == Integer.MAX_VALUE) continue;
            if (bfdpW.distTo(i) == Integer.MAX_VALUE) continue;
            int distance = bfdpV.distTo(i) + bfdpW.distTo(i);
            if (distance < length) {
                vertex = i;
                length = distance;
            }
        }
        return vertex == -1 ? new Pair(-1, -1) : new Pair(vertex, length);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 || v >= digraph.V() || w >= digraph.V()) throw new IllegalArgumentException();
        return bfs(List.of(v), List.of(w)).right;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v >= digraph.V() || w >= digraph.V()) throw new IllegalArgumentException();
        return bfs(List.of(v), List.of(w)).left;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        // It should return -1 (for no such path). by Kevin Wayne
        if (!checkIterable(v) || !checkIterable(w)) return -1;
        return bfs(v, w).right;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (!checkIterable(v) || !checkIterable(w)) return -1;
        return bfs(v, w).left;
    }

    private boolean checkIterable(Iterable<Integer> iterable) {
//        return StreamSupport.stream(iterable.spliterator(), false).findAny().isEmpty();
        // the above isEmpty means iterator == null, which shall throw an error
        int count = 0;
        for (var it : iterable) {
            if (it == null) throw new IllegalArgumentException();
            count++;
        }
        return count > 0;
    }
}
