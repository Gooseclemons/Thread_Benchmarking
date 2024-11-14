package ThreadPerformance;

import java.util.concurrent.ThreadLocalRandom;

public class CustomClient extends Client {

    final RWL lock;

    CustomClient(double writeChance, WikiSystem dataStore, int iterations, RWL lock) {
        super(writeChance, dataStore, iterations);
        this.lock = lock;
    }

    @Override
    void read() {
        lock.readLock();
        try {
            data = dataStore.getData();
        } finally {
            lock.unlockRead();
        }
    }

    @Override
    void write() {
        lock.writeLock();
        try {
            double rand = ThreadLocalRandom.current().nextDouble(0, 1);
            if (rand < 0.5) {   // Add case
                String addString = (rand < 0.25) ? "0" : "1";
                data = data.concat(addString);
                dataStore.setData(data);
            } else if (!data.isEmpty()) {   // Remove case
                data = data.substring(0, data.length() - 1);
                dataStore.setData(data);
            } else {    // Empty case
                data = data.concat("0");
                dataStore.setData(data);
            }
        } finally {
            lock.unlockWrite();
        }
    }

}
