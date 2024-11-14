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

public abstract class Client extends Thread {

    final WikiSystem dataStore;
    final double writeChance;
    final int iterations;
    String data;

    Client(double writeChance, WikiSystem dataStore, int iterations) {
        this.writeChance = writeChance;
        this.dataStore = dataStore;
        this.iterations = iterations;
        data = "";
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            double rand_num = randomNumber(); // Responsible for r/w calculation
            readWriteDecision(rand_num);
        }
    }

    void readWriteDecision(double rand_num) {
        if (rand_num < writeChance) {
            write();
        }
        else {
            read();
        }
    }

    abstract void read();

    abstract void write();

    double randomNumber() {
        return ThreadLocalRandom.current().nextDouble();
    }

}
