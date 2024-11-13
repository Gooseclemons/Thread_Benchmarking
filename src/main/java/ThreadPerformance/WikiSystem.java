package ThreadPerformance;

public class WikiSystem {

    final Client[] clients;
    final RWL lock;

    private String wikiData = "";

    final int num_clients;
    final double writeChance;

    WikiSystem(int num_clients, double writeChance) {
        this.num_clients = num_clients;
        this.writeChance = writeChance;
        lock = new RWL();

        clients = new Client[num_clients];
        initializeClients();
        //runClients()  Is it nescessary to immediately run, difficult for collecting data
    }

    void initializeClients() {
        for (int i = 0; i < num_clients; i++) {
            clients[i] = new Client(writeChance, this, lock);
        }
    }

    void runClients() {
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

