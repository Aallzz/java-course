package ru.ifmo.rain.Petrovski.arrayset;

import java.util.*;

public class ArraySet<T> extends AbstractSet<T> implements NavigableSet<T> {

    private final List<T> array;
    private final Comparator<? super T> comparator;

    private class ReversedList<E> extends AbstractList<E> {
        private final List<E> array;
        private boolean isReversed;

        ReversedList(List<E> list) {
            if (list instanceof ReversedList) {
                array = ((ReversedList<E>) list).array;
                isReversed = !((ReversedList<E>) list).isReversed;
            } else {
                array = list;
                isReversed = true;
            }
        }

        @Override
        public E get(int pos) {
            return isReversed ? array.get(size() - 1 - pos) : array.get(pos);
        }

        @Override
        public int size() {
            return array.size();
        }
    }

    public ArraySet() {
        this(Collections.emptyList(), null);
    }

    public ArraySet(ArraySet<T> other) {
        this(other.array, other.comparator);
    }

    private ArraySet(List<T> list, Comparator<? super T> comparator) {
        array = list;
        this.comparator = comparator;
    }

    public ArraySet(Collection<? extends T> collection) {
        this(collection, null);
    }

    public ArraySet(Collection<? extends T> collection, Comparator<? super T> comparator) {
        Set<T> buffer  = new TreeSet<>(comparator);
        buffer.addAll(collection);
        this.comparator = comparator;
        array = new ArrayList<>(buffer);
    }

    private T getValue(int pos) {
        if (0 <= pos && pos < array.size()) {
            return array.get(pos);
        } else {
            return null;
        }
    }

    private int binarySearch(T element, boolean isStrict, boolean isLower) {
        int pos = Collections.binarySearch(array, element, comparator);
        if (pos >= 0) {
            if (isStrict) {
                return isLower ? pos - 1 : pos + 1;
            } else {
                return pos;
            }
        } else {
            return isLower ? -(pos + 1) - 1 : -(pos + 1);
        }
    }

    @Override
    public T lower(T t) {
        return getValue(binarySearch(t, true, true));
    }

    @Override
    public T floor(T t) {
        return getValue(binarySearch(t, false, true));
    }

    @Override
    public T ceiling(T t) {
        return getValue(binarySearch(t, false, false));
    }

    @Override
    public T higher(T t) {
        return getValue(binarySearch(t, true, false));
    }

    @Override
    public T pollFirst() {
        throw new UnsupportedOperationException("pollFirst");
    }

    @Override
    public T pollLast() throws  UnsupportedOperationException {
        throw new UnsupportedOperationException("pollLast");
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(array).iterator();
    }

    @Override
    public NavigableSet<T> descendingSet() {
        return new ArraySet<>(new ReversedList<>(array), Collections.reverseOrder(comparator));
    }

    @Override
    public Iterator<T> descendingIterator() {
        return descendingSet().iterator();
    }

    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        return tailSet(fromElement, fromInclusive).headSet(toElement, toInclusive);
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        int pos = binarySearch(toElement, !inclusive, true);
        return new ArraySet<>(array.subList(0, pos + 1), comparator);
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        int pos = binarySearch(fromElement, !inclusive, false);
        return new ArraySet<>(array.subList(pos, array.size()), comparator);
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return tailSet(fromElement, true);
    }

    @Override
    public T first() {
        if (array.isEmpty()) {
            throw new NoSuchElementException("Attempt to get element from empty ArraySet");
        }
        return array.get(0);
    }

    @Override
    public T last() {
        if (array.isEmpty()) {
            throw new NoSuchElementException("Attempt to get element from empty ArraySet");
        }
        return array.get(array.size() - 1);
    }

    @Override
    public int size() {
        return array.size();
    }


    @Override
    public boolean contains(Object o) {
        return Collections.binarySearch(array, (T)o, comparator) >= 0;
    }
}


