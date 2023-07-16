import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.ArrayList;
import java.util.Random;

public class SkipListSet<T extends Comparable<T>> implements SortedSet<T> {

    private SkipListItem<T> head; // head of skiplist
    private SkipListItem tail; // tail of skiplist
    private SkipListItem<T> current; // current item being iterated on

    private int size;
    private int rows;
    private int r; // current row

    private Random random = new Random();

    public void printSet() {
        System.out.printf("\n#' Rows: %d\n", rows);
        for (int i = rows - 1; i >= 0; i--) {
            System.out.printf("r%d: ", i);
            head.printRow(i);
            System.out.printf("\n");
        }
        // head.printMaxR();
    }

    // Adds the specified element to this set if it is not already present.
    public boolean add(T e) {
        SkipListItem<T> item = new SkipListItem<T>(e);
        if (head == null) {
            head = item;
            tail = item;
            current = item;
            size = 1;
            rows = head.nRows();
            head.initPointers();
            head.maxR = rows - 1;
            return true;
        }
        boolean res = head.insert(item);
        if (res)
            size++;
        return res;
    }

    // Adds all of the elements in the specified collection to this set if they're
    // not already present.
    public boolean addAll(Collection<? extends T> c) {
        for (T item : c)
            if (!add(item))
                return false;
        return true;
    }

    // Removes all of the elements from this set.
    public void clear() {
        head = null;
        tail = null;
        current = null;

        size = 0;
        rows = 0;
        r = 0; // current row

        random = new Random();
    }

    // Returns the comparator used to order the elements in this set, or null if
    // this set uses the natural ordering of its elements.
    public Comparator<? super T> comparator() {
        return null;
    }

    // Returns true if this set contains the specified element.
    public boolean contains(Object o) {
        SkipListSetIterator<T> it = (SkipListSetIterator<T>) iterator();

        while (it.hasNext()) {
            if (current.value.compareTo(((T) o)) == 0)
                return true;
            current = current.next.get(0);
        }

        return false;
    }

    // Returns true if this set contains all of the elements of the specified
    // collection.
    public boolean containsAll(Collection<?> c) {
        for (T item : (Collection<T>) c)
            if (!contains(item))
                return false;
        return true;
    }

    // Compares the specified object with this set for equality.
    public boolean equals(Object o) {
        if (!(o instanceof SkipListSet))
            return false;

        SkipListSetIterator<T> ti = (SkipListSetIterator<T>) iterator();
        SkipListSetIterator<T> oi = (SkipListSetIterator<T>) ((SkipListSet<T>) o).iterator();

        while (ti.hasNext() && oi.hasNext()) {
            if (current.value != ((SkipListSet<T>) o).current.value)
                return false;
            current = current.next.get(0);
            ((SkipListSet<T>) o).current = ((SkipListSet<T>) o).current.next.get(0);
        }

        return true;
    }

    // Returns the hash code value for this set.
    public int hashCode() {
        return Objects.hashCode(this);
    }

    // Returns true if this set contains no elements.
    public boolean isEmpty() {
        return (size == 0);
    }

    // Returns an iterator over the elements in this set.
    public Iterator<T> iterator() {
        SkipListSetIterator<T> it = new SkipListSetIterator<T>();
        return it;
    }

    // Removes the specified element from this set if it is present.
    public boolean remove(Object o) {
        if (head == null)
            return false;
        boolean res = head.remove((T) o);
        if (res)
            size--;
        return res;
    }

    // Removes from this set all of its elements that are contained in the specified
    // collection.
    public boolean removeAll(Collection<?> c) {
        for (T item : (Collection<T>) c)
            if (!remove(item))
                return false;
        return true;
    }

    // Retains only the elements in this set that are contained in the specified
    // collection.
    public boolean retainAll(Collection<?> c) {
        if (!containsAll(c))
            return false;
        SkipListSetIterator<T> it = (SkipListSetIterator<T>) iterator();

        while (it.hasNext()) {
            if (!c.contains(current))
                remove(current.value);
        }
        return true;
    }

    // Returns the number of elements in this set (its cardinality).
    public int size() {
        return size;
    }

    // Returns a view of the portion of this set whose elements range from
    // fromElement, inclusive, to toElement, exclusive.
    public SkipListSet<T> subSet(T fromElement, T toElement) {
        return null;
    }

    // Returns the first (lowest) element currently in this set.
    public T first() {
        if (head == null)
            return null;
        return head.value;
    }

    // Returns the last (highest) element currently in this set.
    public T last() {
        return (T) tail.value;
    }

    // Returns a view of the portion of this set whose elements are strictly less
    // than toElement
    public SkipListSet<T> headSet(T toElement) {
        return null;
    }

    // Returns a view of the portion of this set whose elements are greater than or
    // equal to fromElement.
    public SkipListSet<T> tailSet(T fromElement) {

        return null;
    }

    // Returns an array containing all of the elements in this set.
    public Object[] toArray() {
        return null;
    }

    // Returns an array containing all of the elements in this set.
    public <T> T[] toArray(T[] a) {
        return null;
    }

    // SkipListSet Constructor
    public SkipListSet() {
        clear();
    }

    private class SkipListSetIterator<T extends Comparable<T>> implements Iterator<T> {
        // Returns true if the iteration has more elements.
        public boolean hasNext() {
            if (current == null)
                return false;
            return (current.next.get(0) != null);
        }

        // Returns the next element in the iteration.
        public T next() {
            if (!hasNext())
                return null;
            return (T) current.next.get(0);
        }

        // Removes current element in iteration.
        public void remove() {
            head.remove(current.value);
            return;
        }

        // Constructor for Iterator
        public SkipListSetIterator() {
            current = head;
        }
    }

    private class SkipListItem<T extends Comparable<T>> {
        private T value = null;

        ArrayList<SkipListItem<T>> prev = null;
        ArrayList<SkipListItem<T>> next = null;

        private int maxR = Integer.MAX_VALUE; // max row item appears at

        // Returns integer that has a 100% chance of being 1, 50% change of being 2,
        // 100/2^n% of being n
        public int nRows() {
            int n = 1;
            while (random.nextInt((int) Math.pow(2, n)) == 0)
                n++;
            return n;
        }

        // Sets max row number to one that is <= # of rows in skip list
        public void setMaxRow() {
            while (maxR > rows)
                maxR = nRows() - 1;
        }

        public boolean removeHelper(T del) {
            if (r == -1)
                return true; // reached and deleted bottom row
            if (value.compareTo(del) == 0) { // this == item
                if (prev.get(r) == null && next.get(r) != null) { // normal head
                    T tempVal = value; // swap old head value with its absolute next value & delete old head value
                    value = next.get(0).value;
                    next.get(0).value = tempVal;
                    return removeHelper(del);
                }

                if (prev.get(r) == null && next.get(r) == null) { // lone head
                    rows--;
                    r--; // drop row & continue
                    return removeHelper(del);
                }

                if (prev.get(r) != null && next.get(r) == null) { // tail
                    prev.get(r).next.set(r, null);
                    if (r == 0 && tail == this)
                        tail = prev.get(r);
                    r--; // drop row & continue
                    return removeHelper(del);
                }

                if (prev.get(r) != null && next.get(r) != null) { // in between
                    prev.get(r).next.set(r, next.get(r)); // prev -> next
                    next.get(r).prev.set(r, prev.get(r)); // prev <- next
                    r--; // drop row & continue
                    return removeHelper(del);
                }
            }

            if (next.get(r) == null) {
                if (r == 0)
                    return false; // del not in list
                r--; // drop row & continue
                return removeHelper(del);
            }

            if (next.get(r).value.compareTo(del) > 0) {
                r--; // drop row & continue
                return removeHelper(del);
            }

            return next.get(r).removeHelper(del);
        }

        public boolean remove(T del) {
            r = rows - 1;
            return removeHelper(del);
        }

        private boolean insertHelper(SkipListItem<T> item) {
            if (r == -1)
                return true; // reached and insert at bottom row
            if (value.compareTo(item.value) == 0)
                return false; // item already exists

            if (prev.get(r) == null && value.compareTo(item.value) > 0) { // this is head & item < this
                T tempVal = value; // swap old head value with item value & insert old head
                value = item.value;
                item.value = tempVal;
                return insert(item);
            }

            if (next.get(r) == null && value.compareTo(item.value) < 0) { // this is tail & item > this
                if (item.maxR >= r) {
                    item.prev.set(r, this); // insert @ end
                    this.next.set(r, item);
                    if (r == 0)
                        tail = item;
                }

                r--; // drop down a row & continue from this node
                return insertHelper(item);
            }

            if (value.compareTo(item.value) < 0 && next.get(r).value.compareTo(item.value) > 0) { // this < item < next
                if (item.maxR >= r) {
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

        public boolean insert(SkipListItem<T> item) {
            r = rows - 1; // start at top row
            item.setMaxRow(); // set max row <= # of rows
            return insertHelper(item);
        }

        public void initPointers() {
            prev = new ArrayList<SkipListItem<T>>(Collections.nCopies(rows, null));
            next = new ArrayList<SkipListItem<T>>(Collections.nCopies(rows, null));
        }

        public void printRow(int row) {
            System.out.printf("%d ", value);
            if (next.get(row) == null)
                return;
            next.get(row).printRow(row);
        }

        public SkipListItem(T value) {
            this.value = value;
            initPointers();
        }
    }

    public static void main(String args[]) {

        SkipListSet<Integer> s = new SkipListSet<Integer>();

        // // Generates SkipLists of level 3 or more
        // for (int j = 0; j < 50; j++) {
        // s = new SkipListSet<Integer>();

        // s.random.setSeed(j);

        // for (int i = 0; i < 10; i++)
        // s.add(Integer.valueOf(s.random.nextInt(100)));

        // if (s.rows >= 3) {
        // System.out.println("----------\nSeed: " + j);
        // s.printSet();
        // System.out.println("\n");
        // }
        // }

        s = new SkipListSet<Integer>();
        s.random.setSeed(5);

        for (int i = 0; i < 10; i++)
            s.add(Integer.valueOf(s.random.nextInt(100)));
        s.printSet();
    }
}
