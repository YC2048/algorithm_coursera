import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        items = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[size] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        Item t = randomShuffle(--size);
        if (size < items.length / 2) {
            resize(items.length / 2);
        }
        return t;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return randomShuffle(size - 1);
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueInterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
//        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
//        StdOut.println(rq.isEmpty());
//        for (int i = 0; i < 10; i++) {
//            rq.enqueue(i);
//        }
//        StdOut.println(rq.size());
//        for (Integer i:rq){
//            StdOut.print(i);
//        }
//        StdOut.println();
//        while(!rq.isEmpty()){
//            StdOut.print(rq.dequeue());
//        }
    }

    private Item randomShuffle(int lastIndex) {
        //no need for exchange
        if (items.length == 0) return items[0];
        // swap the last element & the uniform-random element
        int randomIndex = StdRandom.uniform(lastIndex + 1);
        Item t = items[randomIndex];
        if (randomIndex != lastIndex) {
            items[randomIndex] = items[lastIndex];
            items[lastIndex] = t;
        }
        return t;
    }

    private void resize(int newSize) {
        Item[] t = (Item[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            t[i] = items[i];
        }
        items = t;
    }

    private class RandomizedQueueInterator implements Iterator<Item> {
        private int lastIndex = size - 1;

        public boolean hasNext() {
            return lastIndex >= 0;
        }

        public Item next() {
            return randomShuffle(lastIndex--);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private Item[] items;
    private int size;
}
