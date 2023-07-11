import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.ArrayList;
import java.util.Random;

public class SkipListSet <T extends Comparable<T>> implements SortedSet<T> {

    private SkipListItem<T> head = null;
    public int rows = 0;

    private Random random = new Random();

    public void printSet () {
        System.out.printf("\nRows: %d\n", rows);
        for (int i = 0; i < rows; i++) {
            head.printRow(i);
            System.out.printf("\n");
        }
    }

    // Adds the specified element to this set if it is not already present.
    public boolean add (T e) {
        SkipListItem<T> item = new SkipListItem<T>(e);
        if (head == null) {
            head = item;
            rows = head.nRows();
            head.initPointers();
            return true;
        }
        return head.insert(item);
    }

    // Adds all of the elements in the specified collection to this set if they're not already present.
    public boolean addAll (Collection<? extends T> c) {
        return false;
    }

    // Removes all of the elements from this set. 
    public void clear () {
        return;
    }

    // Returns the comparator used to order the elements in this set, or null if this set uses the natural ordering of its elements.
    public Comparator<? super T> comparator() {
        return null;
    }

    // Returns true if this set contains the specified element.
    public boolean contains (Object o) {
        return false;
    }

    // Returns true if this set contains all of the elements of the specified collection.
    public boolean containsAll (Collection<?> c) {
        return false;
    }

    // Compares the specified object with this set for equality.
    public boolean equals (Object o) {
        return false;
    }

    // Returns the hash code value for this set.
    public int hashCode () {
        return -1;
    }

    // Returns true if this set contains no elements.
    public boolean isEmpty () {
        return false;
    }

    // Returns an iterator over the elements in this set.
    public Iterator<T> iterator () {
        return null;
    }

    // Removes the specified element from this set if it is present.
    public boolean remove (Object o) {
        return false;
    }

    // Removes from this set all of its elements that are contained in the specified collection.
    public boolean removeAll (Collection<?> c) {
        return false;
    }

    // Retains only the elements in this set that are contained in the specified collection.
    public boolean retainAll (Collection<?> c) {
        return false;
    }

    // Returns the number of elements in this set (its cardinality).
    public int size () {
        return 0;
    }

    // Returns a view of the portion of this set whose elements range from fromElement, inclusive, to toElement, exclusive.
    public SkipListSet<T> subSet(T fromElement, T toElement) {
        return null;
    }

    // Returns the first (lowest) element currently in this set.
    public T first () {
        return null;
    }

    // Returns the last (highest) element currently in this set.
    public T last () {
        return null;
    }

    // Returns a view of the portion of this set whose elements are strictly less than toElement
    public SkipListSet<T> headSet (T toElement) {
        return null;
    }

    // Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
    public SkipListSet<T> tailSet (T fromElement) {
        return null;
    }

    // Returns an array containing all of the elements in this set.
    public Object[] toArray() {
        return null;
    }

    // Returns an array containing all of the elements in this set.
    public <T> T[] toArray (T[] a) {
        return null;
    }

    // SkipListSet Constructor
    public SkipListSet () {
    }

    private class SkipListSetIterator<T extends Comparable<T>> implements Iterator<T> {  
        // Returns true if the iteration has more elements.
        public boolean hasNext () {
            return false;
        }

        // Returns the next element in the iteration.
        public T next () {
            return null;
        }

        // Removes current element in iteration.
        public void remove () {
            return;
        }
    }

    private class SkipListItem<T extends Comparable<T>> {
        private T value = null;

        ArrayList<SkipListItem<T>> prev = null;
        ArrayList<SkipListItem<T>> next = null;

        private int r = 0; // current row
        private int maxR = Integer.MAX_VALUE; // max row item appears at

        // Returns integer that has a 100% chance of being 1, 50% change of being 2, 100/2^n% of being n
        public int nRows () {
            int n = 1;
            while (random.nextInt((int)Math.pow(2, n)) == 0)
                n++;
            return n;
        }

        public boolean remove (SkipListItem<T> item) {
            return false;
        }

        private boolean insertHelper (SkipListItem<T> item) {
            if (r == -1) return true; // reached and insert at bottom row
            if (value.compareTo(item.value) == 0) return false; // item already exists

            if (prev.get(r) == null && value.compareTo(item.value) > 0) { // this is head & item < this
                T temp = value; // swap old head value with item value & insert old head
                value = item.value;
                item.value = temp;
                return insert(item);
            }

            if (next.get(r) == null && value.compareTo(item.value) < 0) { // this is tail & item > this
                if (maxR >= r) {
                    item.prev.set(r, this); // insert @ end
                    this.next.set(r, item);
                }

                r--; // drop down a row & continue from this node
                return insertHelper(item);
            }

            if (value.compareTo(item.value) < 0 && next.get(r).value.compareTo(item.value) > 0) { // this < item < next
                if (maxR >= r) {
                    item.prev.set(r, this); // insert in between
                    item.next.set(r, next.get(r));
                    next.get(r).prev.set(r, item);
                    next.set(r, item);
                }

                r--; // drop down a row & continue from this node
                return insertHelper(item);
            }

            return next.get(r).insert(item); // continue to next
        }

        public boolean insert (SkipListItem<T> item) {
            r = rows-1; // start at top row
            while (maxR > rows) maxR = nRows(); // set max row <= # of rows
            return insertHelper(item);
        }

        public void initPointers () {
            prev = new ArrayList<SkipListItem<T>>(Collections.nCopies(rows, null));
            next = new ArrayList<SkipListItem<T>>(Collections.nCopies(rows, null));
        }

        public void printRow (int row) {
            System.out.printf("%d ", value);
            if (next.get(row) == null) return;
            next.get(row).printRow(row);
        }

        public SkipListItem (T value) {
            this.value = value;
            initPointers();
        }
    }

    public static void main (String args[]) {
        SkipListSet<Integer> s = new SkipListSet<Integer>();

        for (int j = 0; j < 50; j++) {
            s = new SkipListSet<Integer>();
            
            for(int i = 0; i < 10; i++)
                s.add(Integer.valueOf(s.random.nextInt(100)));

            s.printSet();
        }
    }
}
