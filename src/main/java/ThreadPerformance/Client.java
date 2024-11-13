package ThreadPerformance;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

//@State(Scope.Benchmark)
//@OutputTimeUnit(TimeUnit.MICROSECONDS)
//@BenchmarkMode(Mode.All)
//public class Client {
//    @Benchmark
//    public Integer[] init() {
//        return new Integer[256];
//    }
//}

import java.util.concurrent.ThreadLocalRandom;

public class Client extends Thread {

    final WikiSystem dataStore;
    final RWL lock;

    final double writeChance;
    String data;

    Client(double writeChance, WikiSystem dataStore, RWL lock) {
        this.writeChance = writeChance;
        this.dataStore = dataStore;
        this.lock = lock;
        data = "";
    }

    @Override
    public void run() {
        double rand_num = randomNumber(); // Responsible for r/w calculation
        if (rand_num < writeChance) { // Write case
            lock.writeLock(); // Acquire lock

            data = dataStore.getData();
            if (rand_num < (writeChance / 2)) { // Add condition
                data = data.concat("0");
            }
            else if (!data.isEmpty()) { // Remove condition
                data = data.substring(0, data.length() - 1);
            }
            else if (data.isEmpty() || data.length() > 64) {  // Empty or too large condition
                data = "0";
            }
            dataStore.setData(data);
            //System.out.println(data); // Debuggg

            lock.unlockWrite(); // Release lock
        }
        else { // Read case
            lock.readLock();
            data = dataStore.getData();
            lock.unlockRead();
        }
    }

    double randomNumber() {
        return ThreadLocalRandom.current().nextDouble();
    }

}
