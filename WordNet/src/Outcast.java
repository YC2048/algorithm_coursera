public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        if (nouns == null) throw new IllegalArgumentException();
        if (nouns.length == 1) return nouns[0];

        int leastDis = 0, index = 0;
        for (int i = 0; i < nouns.length; i++) {
            int total = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i == j) continue;
                total += wordNet.distance(nouns[i], nouns[j]);
            }
            if (leastDis < total) {
                leastDis = total;
                index = i;
            }
        }
        return nouns[index];
    }
}
