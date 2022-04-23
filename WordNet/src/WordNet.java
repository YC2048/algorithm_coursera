import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WordNet {
    private final HashMap<String, Set<Integer>> nounToVertex;
    private final HashMap<Integer, String> vertexToNouns;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        this.nounToVertex = new HashMap<>();
        this.vertexToNouns = new HashMap<>();
        HashMap<Integer, Integer> indexToVertex = new HashMap<>();
        int vertexCount = 0;
        var in = new edu.princeton.cs.algs4.In(synsets);
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            String[] synonyms = line[1].split(" ");
            vertexToNouns.put(vertexCount, line[1]);
            indexToVertex.put(Integer.parseInt(line[0]), vertexCount);

            for (var synonym : synonyms) {
                if (!nounToVertex.containsKey(synonym)) {
//                    nounToVertex.put(synonym, Set.of(vertexCount));
                    // ambiguous between Set.of(int size) and Set.of(Elem e)
                    Set<Integer> set = new HashSet<>();
                    set.add(vertexCount);
                    nounToVertex.put(synonym, set);
                } else {
                    nounToVertex.get(synonym).add(vertexCount);
                }
            }

            vertexCount++;
        }
        Digraph digraph = new Digraph(vertexCount);
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            int from = indexToVertex.get(Integer.parseInt(line[0]));
            for (int i = 1; i < line.length; i++) {
                digraph.addEdge(from, indexToVertex.get(Integer.parseInt(line[i])));
            }
        }
        checkDigraph(digraph);
        this.sap = new SAP(digraph);
    }

    private void checkDigraph(edu.princeton.cs.algs4.Digraph digraph) {
        if (new DirectedCycle(digraph).hasCycle()) throw new IllegalArgumentException();
        if (!new Topological(digraph).hasOrder()) throw new IllegalArgumentException();
        int rootCount = 0;
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0) rootCount++;
        }
        if (rootCount != 1) throw new IllegalArgumentException();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToVertex.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounToVertex.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !this.isNoun(nounA) || !this.isNoun(nounB))
            throw new IllegalArgumentException();
        if (nounA.equals(nounB)) return 0;
        var verticesA = nounToVertex.get(nounA);
        var verticesB = nounToVertex.get(nounB);
        return sap.length(verticesA, verticesB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in the shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !this.isNoun(nounA) || !this.isNoun(nounB))
            throw new IllegalArgumentException();
        var verticesA = nounToVertex.get(nounA);
        var verticesB = nounToVertex.get(nounB);
        return vertexToNouns.get(sap.ancestor(verticesA, verticesB));
    }
}
