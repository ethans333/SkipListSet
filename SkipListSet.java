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
    public int iterations = 0;

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

    // Adds e into skip list at a random set of levels
    // Returns true if successful, false otherwise
    // e: element to be inserted
    public boolean add(T e) {
        if (head == null) {
            head = new SkipListItem<T>(e);
            tail = head;
            current = head;
            size = 1;
            rows = head.nRows();
            head.initPointers(); // prev<T>[rows], next<T>[rows]
            head.maxR = rows - 1;
            return true;
        } else if (rows < (int) (Math.log(size + 1) / Math.log(2))) {
            rows = (int) (Math.log(size + 1) / Math.log(2));
            head.initPointers(); // Re-init pointers with new, greater height
        }
        SkipListItem<T> item = new SkipListItem<T>(e);
        boolean res = head.insert(item);
        if (res)
            size++;
        return res;
    }

    // Adds all of the elements in the specified collection to this set if they're
    // not already present.
    // Returns true if adding all elements was successful, false otherwise.
    // c: collection of types T, to be inserted into skiplist
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
    // c: collection of items to be iterated over
    public boolean containsAll(Collection<?> c) {
        for (T item : (Collection<T>) c)
            if (!contains(item))
                return false;
        return true;
    }

    // Compares the specified object with this set for equality.
    // Returns true if o equals this skiplistset, false otherwise.
    // o: skiplistset to be compared to
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
    // Returns true if remove was successful, false otherwise.
    // o: object to be removed.
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
    // Returns true if all removes were successful, false otherwise.
    // c: collection of types T to be remove from skiplistset.
    public boolean removeAll(Collection<?> c) {
        for (T item : (Collection<T>) c)
            if (!remove(item))
                return false;
        return true;
    }

    // Retains only the elements in this set that are contained in the specified
    // collection.
    // If retention was successful returns true, false otherwise.
    // c : collection of elements in the set to be retained.
    public boolean retainAll(Collection<?> c) {
        if (!containsAll(c))
            return false;
        SkipListSetIterator<T> it = (SkipListSetIterator<T>) iterator();

        while (it.hasNext()) {
            if (!c.contains(current))
                remove(current.value);
            current = current.next.get(0);
        }
        return true;
    }

    // Returns the number of elements in this set (its cardinality).
    public int size() {
        return size;
    }

    // Returns a view of the portion of this set whose elements range from
    // fromElement, inclusive, to toElement, exclusive.
    // Returns subset if sucessful, null otherwise.
    public SkipListSet<T> subSet(T fromElement, T toElement) {
        if (toElement.compareTo(fromElement) < 0)
            return null;
        SkipListSet<T> ss = new SkipListSet<T>();
        SkipListSetIterator<T> it = (SkipListSetIterator<T>) iterator();

        while (it.hasNext()) {
            if (current.value.compareTo(fromElement) >= 0 && current.value.compareTo(toElement) <= 0)
                ss.add(current.value); // if between from and to
            current = current.next.get(0);
        }
        return ss;
    }

    // Returns the first (lowest) element currently in this set.
    // Returns null if no elements are in the set.
    public T first() {
        if (head == null)
            return null;
        return head.value;
    }

    // Returns the last (highest) element currently in this set.
    // Returns null if no elements are in the set.
    public T last() {
        return (T) tail.value;
    }

    // Returns a view of the portion of this set whose elements are strictly less
    // than toElement.
    // Returns subset if successful, null otherwise.
    public SkipListSet<T> headSet(T toElement) {
        SkipListSet<T> ss = new SkipListSet<T>();
        SkipListSetIterator<T> it = (SkipListSetIterator<T>) iterator();

        while (it.hasNext() && current.value.compareTo(toElement) <= 0) {
            ss.add(current.value); // if current < toElement, add
            current = current.next.get(0);
        }
        return ss;
    }

    // Returns a view of the portion of this set whose elements are greater than or
    // equal to fromElement.
    // Returns subset if successful, null otherwise.
    public SkipListSet<T> tailSet(T fromElement) {
        SkipListSet<T> ss = new SkipListSet<T>();
        SkipListSetIterator<T> it = (SkipListSetIterator<T>) iterator();

        while (it.hasNext() && current.value.compareTo(fromElement) >= 0) {
            ss.add(current.value); // if current < toElement, add
            current = current.next.get(0);
        }
        return ss;
    }

    // Returns an array containing all of the elements in this set.
    public Object[] toArray() {
        SkipListSetIterator<T> it = (SkipListSetIterator<T>) iterator();
        Object[] arr = new Object[size()];

        int i = 0;

        while (it.hasNext()) {
            arr[i++] = current.value;
            current = current.next.get(0);
        }

        return arr;
    }

    // Returns an array containing all of the elements in this set.
    public <T> T[] toArray(T[] a) {
        return null;
    }

    // Re-arranges elements with a log n ascension
    public void reBalance() { // Should be linear
        SkipListSetIterator<T> it = (SkipListSetIterator<T>) iterator();

        SkipListItem<T> prev = null;
        SkipListItem<T> next = null;

        int i = 1, j = 0, k = 0;

        while (current != null) {
            current.maxR = 0;

            for (j = rows - 1; j >= 0; j--) {
                if (i == 1) {
                    current.maxR = rows - 1;
                    break;
                } else if (i >= Math.pow(2, j + 1) && i % Math.pow(2, j + 1) == 0) {
                    current.maxR = j;
                    break;
                }
            }

            prev = current.prev.get(0);
            next = current.next.get(0);

            current.prev = new ArrayList<SkipListItem<T>>(Collections.nCopies(current.maxR + 1, null));
            current.next = new ArrayList<SkipListItem<T>>(Collections.nCopies(current.maxR + 1, null));

            current.prev.set(0, prev);
            current.next.set(0, next);

            for (j = current.maxR; j > 0; j--) {
                prev = current;
                next = current;

                for (k = 0; k < (int) Math.pow(2, j + 1) + ((i == 1) ? -1 : 0); k++) {
                    prev = (prev == null) ? null : prev.prev.get(0);
                    next = (next == null) ? null : next.next.get(0);
                }

                current.prev.set(j, prev == current ? null : prev);
                current.next.set(j, next == current ? null : next);
            }

            i++;

            current = current.next.get(0);
        }
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

        private int maxR = Integer.MAX_VALUE; // 0 to n-1

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

        // Irretively removes element item in the skip list set
        // item: Item to be removed
        // returns true if deletion was successful, false otherwise
        public boolean remove(T del) {
            r = rows - 1;
            SkipListItem<T> current = this;

            while (r > -1) { // Iterating on valid row
                if (current.value.compareTo(del) == 0) { // current == item
                    if (current.prev.get(r) == null && current.next.get(r) != null) { // normal head
                        T tempVal = current.value; // swap old head value its next value & delete old head
                        current.value = current.next.get(0).value;
                        current.next.get(0).value = tempVal;
                        continue;
                    }

                    if (current.prev.get(r) == null && current.next.get(r) == null) { // lone head
                        rows--;
                        r--; // drop row & continue
                        continue;
                    }

                    if (current.prev.get(r) != null && current.next.get(r) == null) { // tail
                        current.prev.get(r).next.set(r, null);
                        if (r == 0 && tail == this)
                            tail = current.prev.get(r);
                        r--; // drop row & continue
                        continue;
                    }

                    if (prev.get(r) != null && next.get(r) != null) { // in between
                        prev.get(r).next.set(r, next.get(r)); // prev -> next
                        next.get(r).prev.set(r, prev.get(r)); // prev <- next
                        r--; // drop row & continue
                        continue;
                    }
                }

                if (current.next.get(r) == null) {
                    if (r == 0)
                        return false; // del not in list
                    r--; // drop row & continue
                    continue;
                }

                if (current.next.get(r).value.compareTo(del) > 0) {
                    r--; // drop row & continue
                    continue;
                }

                current = current.next.get(r);
            }

            return true;
        }

        // Irretively adds element item into skip list set
        // item: Item to be inserted
        // returns true if insertion was successful, false otherwise
        public boolean insert(SkipListItem<T> item) {
            r = rows - 1; // start at top row
            item.setMaxRow(); // set max row <= # of rows

            SkipListItem<T> current = this;

            while (r > -1) { // Iterating on valid row
                if (current.value.compareTo(item.value) == 0)
                    return false; // item already exists
                if (current.prev.get(r) == null && current.value.compareTo(item.value) > 0) {
                    // head & item < current
                    T tempVal = current.value; // swap old head value with item value
                    current.value = item.value;
                    item.value = tempVal;
                    return ((SkipListItem<T>) head).insert(item); // insert old head
                }
                if (current.next.get(r) == null && current.value.compareTo(item.value) < 0) {
                    // tail & item > current
                    if (item.maxR >= r) {
                        item.prev.set(r, current); // insert @ end
                        current.next.set(r, item);
                        if (r == 0)
                            tail = item;
                    }

                    r--; // drop down a row & continue from current
                    continue;
                }
                if (current.value.compareTo(item.value) < 0 &&
                        current.next.get(r).value.compareTo(item.value) > 0) {
                    if (item.maxR >= r) {
                        item.prev.set(r, current); // insert in between
                        item.next.set(r, current.next.get(r));
                        current.next.get(r).prev.set(r, item);
                        current.next.set(r, item);
                    }

                    r--; // drop down a row & continue from current
                    continue;
                }
                current = current.next.get(r);
            }

            return true;
        }

        // Initializes next and previous pointer arrays for skip list item
        public void initPointers() {
            if (prev == null && next == null) {
                prev = new ArrayList<SkipListItem<T>>(Collections.nCopies(rows, null));
                next = new ArrayList<SkipListItem<T>>(Collections.nCopies(rows, null));
                return;
            } else {
                // Reinitialize pointers with a bigger size
                ArrayList<SkipListItem<T>> nPrev = new ArrayList<SkipListItem<T>>(Collections.nCopies(rows, null));
                ArrayList<SkipListItem<T>> nNext = new ArrayList<SkipListItem<T>>(Collections.nCopies(rows, null));

                for (int i = 0; i < prev.size(); i++) {
                    nPrev.set(i, prev.get(i));
                    nNext.set(i, next.get(i));
                }

                prev = nPrev;
                next = nNext;
            }
        }

        // Prints each row of skiplist set, used for troubleshooting
        public void printRow(int row) {
            System.out.printf("%d ", value);
            if (next.size() < row || next.get(row) == null)
                return;
            next.get(row).printRow(row);
        }

        // Constructor for skiplistitem
        public SkipListItem(T value) {
            this.value = value;
            initPointers();
        }
    }
}
