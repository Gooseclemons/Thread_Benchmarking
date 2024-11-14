package ThreadPerformance;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WikiSystem {

    final Client[] clients;
    final RWL customLock;
    final ReentrantReadWriteLock platformLock;

    private String wikiData = "";

    final int num_clients;
    final double writeChance;
    final int iterations;

    WikiSystem(int num_clients, double writeChance, int iterations) {
        this.num_clients = num_clients;
        this.writeChance = writeChance;
        customLock = new RWL();
        platformLock = new ReentrantReadWriteLock();
        clients = new Client[num_clients];
        this.iterations = iterations;
    }

    void initializePlatformClients() {
        for (int i = 0; i < num_clients; i++) {
            clients[i] = new PlatformClient(writeChance, this, iterations, platformLock);
        }
    }

    void runPlatformClients() {
        for (int i = 0; i < num_clients; i++) {
            clients[i].start();
        }
    }

    void initializeCustomClients() {
        for (int i = 0; i < num_clients; i++) {
            clients[i] = new CustomClient(writeChance, this, iterations, customLock);
        }
    }

    void runCustomClients() {
        for (int i = 0; i < num_clients; i++) {
            clients[i].start();
        }
    }

    String getData() {
        return wikiData;
    }

    void setData(String data) {
        wikiData = data;
    }



}

