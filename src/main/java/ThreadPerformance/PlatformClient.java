package ThreadPerformance;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PlatformClient extends Client {

    final ReentrantReadWriteLock lock;

    PlatformClient(double writeChance, WikiSystem dataStore, int iterations, ReentrantReadWriteLock lock) {
        super(writeChance, dataStore, iterations);
        this.lock = lock;
    }

    @Override
    void read() {
        lock.readLock().lock();
        try {
            data = dataStore.getData();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    void write() {
        lock.writeLock().lock();
        try {
            double rand = ThreadLocalRandom.current().nextDouble(0, 1);
            if (rand < 0.5) {   // Add case
                String addString = (rand < 0.25) ? "0" : "1";
                data = data.concat(addString);
            } else if (!data.isEmpty()) {   // End case
                data = data.substring(0, data.length() - 1);
            } else {    // Empty case
                data = data.concat("0");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

}
