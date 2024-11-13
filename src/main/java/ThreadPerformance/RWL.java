package ThreadPerformance;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A Conservative implementation for a Read-Write Locking mechanism
 *
 */
public class RWL {

    final ReentrantLock lock = new ReentrantLock();

    // Conditions for managing lock based on read/write operations
    final Condition readable = lock.newCondition();
    final Condition writeable = lock.newCondition();

    int readers, writers; // Initializes automatically to 0
    int waitingReaders;

    /**
     * A method for providing a lock to a thread attempting to write
     * Method first awaits for no readers OR writers to be active before incrementing the writers count to 1 to prevent
     *  any more locks of either readers or writers from being granted until writer completes its operation.
     */
    void writeLock() {
        lock.lock();
        try {
            while (readers > 0 || writers > 0) {
                writeable.awaitUninterruptibly();
            }
            writers = 1;
        } finally {
            lock.unlock();
        }
    }

    /**
     * A method for providing a lock to a thread attempting to read
     * Method waits until there are no active writers, done by checking the "writers" variables value, and then
     *  establishes a lock by incrementing "readers" variables count by 1, preventing any writers from claiming a lock.
     */
    void readLock() {
        lock.lock();
        try {
            waitingReaders++;
            while (writers > 0) {
                readable.awaitUninterruptibly();
            }
            waitingReaders--;
            readers++;
        } finally {
            lock.unlock();
        }
    }

    /**
     * A Method for releasing a lock held by a writer thread.
     * Method returns "writers" variable count back to 0 before waking all threads currently waiting on the writer
     *  thread to complete.
     * Uses a waitingReaders/Writers variable to track if there are any waiting readers. If there are no waiting
     *  readers, a single writer thread is awakened, else wake all readers
     */
    void unlockWrite() {
        lock.lock();
        try {
            writers = 0;
            if (waitingReaders == 0) {
                writeable.signal();
            }
            else {
                readable.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * A Method for releasing a lock held by a reader thread.
     * Method decrements the "readers" variables count and if there are no remaining readers then wake a single writer
     */
    void unlockRead() {
        lock.lock();
        try {
            if (--readers == 0) {
                writeable.signal();
            }
        } finally {
            lock.unlock();
        }
    }

}
