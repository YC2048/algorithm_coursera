import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;

public class Solver {
    private boolean isSolvable;
    private int minimumMove;
    private ArrayList<Board> solution;

    private class Node {
        public Node parent;
        public Board board;
        public int move;
        public int manhattan;
        public boolean isGoal;

        Node(Node parent, Board cur, int move) {
            this.parent = parent;
            this.board = cur;
            this.move = move;
            this.manhattan = cur.manhattan();
            this.isGoal = cur.isGoal();
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        // initialization
        minimumMove = -1;
        solution = new ArrayList<>();
        // find goal
        if (initial.isGoal()) {
            minimumMove = 0;
            isSolvable = true;
            solution.add(initial);
        } else {
            solve(initial);
        }
    }

    private void solve(Board initial) {
        Node initialNode = new Node(null, initial, 0);
        Node twinNode = new Node(null, initial.twin(), 0);
        MinPQ<Node> openList = new MinPQ<>(Comparator.comparingInt(o -> o.manhattan + o.move));
        MinPQ<Node> twinList = new MinPQ<>(Comparator.comparingInt(o -> o.manhattan + o.move));
        Node goal = null;
        openList.insert(initialNode);
        twinList.insert(twinNode);
        while (!openList.isEmpty() && !twinList.isEmpty()) {
            Node cur = openList.delMin();
            Node twin = twinList.delMin();
            if (cur.isGoal) {
                goal = cur;
                break;
            }
            if (twin.isGoal) {
                solution = null;
                isSolvable = false;
                minimumMove = -1;
                return;
            }
            for (Board child : cur.board.neighbors()) {
                if (trackBackDuplicate(cur, child)) continue;
                openList.insert(new Node(cur, child, cur.move + 1));
            }
            for (Board child : twin.board.neighbors()) {
                if (trackBackDuplicate(twin, child)) continue;
                twinList.insert(new Node(twin, child, twin.move + 1));
            }
        }

        if (goal != null) {
            isSolvable = true;
            minimumMove = goal.move;
            solution.add(goal.board);
            while (goal.parent != null) {
                goal = goal.parent;
                solution.add(0, goal.board);
            }
        } else {
            solution = null;
            isSolvable = false;
            minimumMove = -1;
        }
    }

    private boolean trackBackDuplicate(Node parent, Board cur) {
        return parent.board.equals(cur);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable ? minimumMove : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return this.isSolvable ? this.solution : null;
    }

    // test client (see below)
    public static void main(String[] args) {

    }

}
