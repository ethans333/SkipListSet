import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.ArrayList;
import java.util.Random;

public class SkipListSet <T extends Comparable<T>> implements SortedSet<T> {

    ArrayList<SkipListItem<T>> levels = new ArrayList<SkipListItem<T>>();

    // Generates skip list levels. The probability of a new level being generated decreases by 1/2
    private boolean genLevels (SkipListItem<T> head) {
        if (head == null) return false;

        Random random = new Random();
        int bound = 2;

        levels.add(head); // Adds at least one level
        while (random.nextInt(bound-1) == random.nextInt(bound-1)) { // Probabilty of adding a new level decreases by 1/2
            levels.add(head);
            bound *= 2;
        }

        return true;
    }

    // Adds the specified element to this set if it is not already present.
    public boolean add (T e) {
        SkipListItem<T> item = new SkipListItem(e);
        if (isEmpty()) genLevels(item);

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
        return (levels.size() == 0);
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
        SkipListItem<T> prev = null;
        SkipListItem<T> next = null;

        T payload = null;

        public SkipListItem (T payload) {
            this.payload = payload;
        }
    }
}
