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
            arr[i] = current.value;
            current = current.next.get(0);
        }

        return arr;
    }

    // Returns an array containing all of the elements in this set.
    public <T> T[] toArray(T[] a) {
        return null;
    }

    public void reBalance() { // Should be linear
        rows = (int)(Math.log(size)/Math.log(2));
        
        SkipListSetIterator<T> it = (SkipListSetIterator<T>) iterator();
        SkipListItem<T> back = null;
        SkipListItem<T> tempPrev = null;
        SkipListItem<T> tempNext = null;

        while (it.hasNext()) {
            current.setMaxRow();
            tempPrev = current.prev.get(0);
            tempNext = current.next.get(0);
            current.initPointers();
            current.prev.set(0, tempPrev);
            current.next.set(0, tempNext);

            if (current == head) current.maxR = rows;
            if (current.maxR > 1) {
                back = current.prev.get(0);
                for (int row = 1; row < current.maxR; row++) {
                    if (back == null) break;
                    while (back.maxR-1 < row)
                        back = back.prev.get(0);
                    current.prev.set(row, back);
                    back.next.set(row, current);
                }
            }

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

        public boolean remove(T del) {
            r = rows - 1;
            SkipListItem<T> current = this;

            while (r > -1) {
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

        public boolean insert(SkipListItem<T> item) {
            iterations = 0;
            r = rows - 1; // start at top row
            item.setMaxRow(); // set max row <= # of rows

            SkipListItem<T> current = this;

            while (r > -1) {
                iterations++;
                if (current.value.compareTo(item.value) == 0)
                    return false; // item already exists
                if (current.prev.get(r) == null && current.value.compareTo(item.value) > 0) {
                    // head & item < current
                    T tempVal = current.value; // swap old head value with item value
                    current.value = item.value;
                    item.value = tempVal;
                    return ((SkipListItem<T>)head).insert(item); // insert old head
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

        // Generates SkipLists of level 3 or more
        // int j = 0;
        // while (true) {
        // s = new SkipListSet<Integer>();

        // s.random.setSeed(j);

        // for (int i = 0; i < 100000; i++) {
        // try {
        // s.add(Integer.valueOf(s.random.nextInt(100)));
        // } catch (StackOverflowError e) {
        // System.out.println("Seed: " + j);
        // s.printSet();
        // }
        // }

        // if (s.rows >= 3) {
        // System.out.println("----------\nSeed: " + j);
        // s.printSet();
        // System.out.println("\n");
        // }

        // j = s.random.nextInt();
        // }

        s.random.setSeed(5);

        for (int i = 0; i < 100; i++)
            s.add(Integer.valueOf(s.random.nextInt(100)));
        s.printSet();

        System.out.println("Size: " + s.size());

        s.reBalance();

        s.printSet();
    }
}
