import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.ArrayList;
import java.util.Random;

public class SkipListSet <T extends Comparable<T>> implements SortedSet<T> {

    private SkipListItem<T> head = null;
    public int rows = 0;

    private Random random = new Random();

    // Adds the specified element to this set if it is not already present.
    public boolean add (T e) {
        if (head == null) {
            head = new SkipListItem<T>(e);
            rows = head.nRows();
            return true;
        }

        return true;
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

        ArrayList<SkipListItem<T>> prev = new ArrayList<SkipListItem<T>>();
        ArrayList<SkipListItem<T>> next = new ArrayList<SkipListItem<T>>();

        private int r = 0; // current row

        // Returns integer that has a 100% chance of being 1, 50% change of being 2, 100/2^n% of being n
        public int nRows () {
            int r = 1;
            while (random.nextInt((int)Math.pow(2, r)) == 0)
                r++;
            return r;
        }

        public boolean remove (SkipListItem<T> item) {
            return false;
        }

        // Recursively inserts item into skip list on a current row
        // item is item to be inserted into row
        // returns true if item inserted, false if item already exists in row
        private boolean insertHelper (SkipListItem<T> item, int maxRow) {
            if (value.compareTo(item.value) == 0) return false; // item already exists

            if (value.compareTo(item.value) < 0) {  // this < item
                if (prev.get(r) == null) { // @ beginning of row
                    T temp = value; // swap value w/ item.value then insert old value
                    value = item.value;
                    item.value = temp;
                    return insert(item);
                }

                item.next.set(r, this); // insert in between
                item.prev.set(r, prev.get(r));
                prev.get(r).next.set(r, item);
                prev.set(r, item);
                return true;
            }

            if (next.size() == r) { // @ end of row
                next.add(item); // insert at end
                item.prev.add(this);
                return true;
            }

            return next.get(r).insertHelper(item, maxRow); // continue to next node
        }

        // determines max row item will be inserted at and then inserts it into each row at or below max row
        // item is item to be inserted into the skip list
        // returns true if insertion was successful, false otherwise
        public boolean insert (SkipListItem<T> item) {
            int maxRow = Integer.MAX_VALUE;
            while (maxRow > rows) maxRow = nRows(); // set max row to be row <= rows of skip list

            for (r = rows; r >= 0; r--) insert(item, maxRow); // insert item at and below maxRow
            return true;
        }

        public SkipListItem (T value) {
            this.value = value;
        }
    }

    public static void main (String args[]) {
        SkipListSet<Integer> sls = new SkipListSet<Integer>();
    }
}
