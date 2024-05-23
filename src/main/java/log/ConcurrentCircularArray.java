package log;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Concurrent circular array implementation
 * Provides methods to add element, get current size, get sublist and get whole array as iterable
 */
public class ConcurrentCircularArray implements Iterable<LogEntry> {
    private final LogEntry[] underlying;
    private final int capacity;
    private int size;
    private int head;
    private int tail;
    private final Lock lock = new ReentrantLock();

    /**
     * Initialize instance with capacity
     *
     * @param capacity - maximum list capacity
     */
    public ConcurrentCircularArray(int capacity) {
        this.underlying = new LogEntry[capacity];
        this.capacity = capacity;
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }

    /**
     * Add entry to the list
     *
     * @param entry - log entry
     */
    public void add(LogEntry entry) {
        lock.lock();
        try {
            if (size == underlying.length) {
                head = (head + 1) % underlying.length; // удаление старого элемента
            } else {
                size++;
            }
            underlying[tail] = entry;
            tail = (tail + 1) % underlying.length;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        return size;
    }

    /**
     * Get subarray iterator from whole array
     *
     * @param startFrom - inclusive start index
     * @param indexTo   - exclusive end index
     * @return an iterator of the specified range within this array
     */
    public Iterator<LogEntry> subArrayIterator(int startFrom, int indexTo) {
        return new CircularArrayIterator(startFrom, indexTo);
    }

    @Override
    public Iterator<LogEntry> iterator() {
        return new CircularArrayIterator(0, size);
    }

    private class CircularArrayIterator implements Iterator<LogEntry> {
        private int current = 0;
        private final int start;
        private final int end;

        public CircularArrayIterator(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean hasNext() {
            lock.lock();
            try {
                return current < end - start;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public LogEntry next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lock.lock();
            try {
                int index = (head + start + current) % size;
                current += 1;
                return underlying[index];
            } finally {
                lock.unlock();
            }
        }
    }
}
