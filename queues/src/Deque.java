import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    public Deque() {
        first = last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return 0 == size;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        Node node = new Node(item);
        if (isEmpty()) {
            first = last = node;
        } else {
            node.next = first;
            first.prev = node;
            first = node;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        Node node = new Node(item);
        if (isEmpty()) {
            first = last = node;
        } else {
            node.prev = last;
            last.next = node;
            last = node;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = first.item;
        if (first == last) {
            last = null;
            first = null;
        } else {
            first = first.next;
            first.prev.next = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = last.item;
        if (first == last) {
            first = last = null;
        } else {
            last = last.prev;
            last.next.prev = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
//        Deque<Character> dq = new Deque<>();
//        dq.addFirst('a');
//        dq.addFirst('b');
//        dq.addFirst('c');
//        dq.addFirst('d');
//        for (Character c:dq){
//            StdOut.print(c);
//        }
//        StdOut.println();
//        while(!dq.isEmpty()){
//            StdOut.print(dq.removeFirst());
//        }
//        StdOut.println();
//        dq.addLast('a');
//        dq.addLast('b');
//        dq.addLast('c');
//        dq.addLast('d');
//        StdOut.println(dq.size());
//        for (Character c:dq){
//            StdOut.print(c);
//        }
//        StdOut.println();
//        while(!dq.isEmpty()){
//            StdOut.print(dq.removeLast());
//        }
//        StdOut.println();
    }

    private class Node {
        Item item;
        Node prev;
        Node next;

        public Node(Item item) {
            if (item == null) throw new IllegalArgumentException();
            this.item = item;
            this.next = this.prev = null;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node cur = first;

        public boolean hasNext() {
            return cur != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = cur.item;
            cur = cur.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // construct an empty deque
    private int size;
    private Node first, last;
}
