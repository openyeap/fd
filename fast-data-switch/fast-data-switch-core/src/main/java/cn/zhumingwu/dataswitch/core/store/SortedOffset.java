package cn.zhumingwu.dataswitch.core.store;

import lombok.var;

import java.util.*;

/**
 * Offset of file or topic
 * <p>fast to get file id of the topic or topic offset in the file.
 *
 * @author zhumingwu
 * @since 2022/1/27 17:34
 */
public class SortedOffset {


    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     */
    transient OffsetElement[] elementData; // non-private to simplify nested class access

    /**
     * The size of the ArrayList (the number of elements it contains).
     *
     * @serial
     */
    private int size;

    public SortedOffset() {
        this.elementData = new OffsetElement[8];
    }

    public SortedOffset(int initialCapacity) {
        if (initialCapacity == 0) {
            this.elementData = new OffsetElement[8];
        } else if (initialCapacity > 0) {
            this.elementData = new OffsetElement[initialCapacity];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
    }

    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Increases the capacity to ensure that it can hold at least the
     * number of elements specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    private void ensureCapacityInternal(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    public int size() {
        return size;
    }

    public OffsetElement[] toArray() {
        return (OffsetElement[]) Arrays.copyOf(elementData, size);
    }


    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public OffsetElement get(int index) {
        rangeCheck(index);
        return this.elementData[index];
    }

    public void sort() {
        Comparator<? super OffsetElement> c = new Comparator<OffsetElement>() {
            @Override
            public int compare(OffsetElement o1, OffsetElement o2) {
                return Long.compare(o1.offset, o2.offset);
            }
        };
        Arrays.sort(elementData, 0, size, c);
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param element element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean add(OffsetElement element) {
        ensureCapacityInternal(size + 1);
        elementData[size++] = element;
        return true;
    }


    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the
     * specified collection's Iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified collection is this list, and this
     * list is nonempty.)
     *
     * @param element collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    public boolean addAll(OffsetElement... element) {
        int numNew = element.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount
        System.arraycopy(element, 0, elementData, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    public int search(long offset) {
        return binarySearch(0, this.size, offset);
    }

    private int binarySearch(int fromIndex, int toIndex, long offset) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            var midVal = this.elementData[mid];
            int cmp = midVal.compareTo(offset);
            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }

    /**
     * Checks if the given index is in range.  If not, throws an appropriate
     * runtime exception.  This method does *not* check if the index is
     * negative: It is always used immediately prior to an array access,
     * which throws an ArrayIndexOutOfBoundsException if index is negative.
     */
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * Constructs an IndexOutOfBoundsException detail message.
     * Of the many possible refactorings of the error handling code,
     * this "outlining" performs best with both server and client VMs.
     */
    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }


    public static class OffsetElement implements Comparable<Long> {
        final long offset;
        final int size;

        final long fileId;
        final long position;

        public OffsetElement(long offset, int size, long fileId, long position) {
            this.offset = offset;
            this.size = size;
            this.fileId = fileId;
            this.position = position;
        }

        @Override
        public int compareTo(Long o) {
            if (o < this.offset) {
                return -1;
            }
            if (o > this.offset + this.size) {
                return 1;
            }
            return 0;
        }
    }
}
