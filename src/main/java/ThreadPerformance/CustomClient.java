package ThreadPerformance;

public class CustomClient extends Client {

    final RWL lock;

    CustomClient(double writeChance, WikiSystem dataStore, int iterations, RWL lock) {
        super(writeChance, dataStore, iterations);
        this.lock = lock;
    }

    @Override
    void read() {

    }

    @Override
    void write() {

    }

}
