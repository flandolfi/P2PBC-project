package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Cache;
import it.unipi.di.p2pbc.newscast.core.ConstAgent;

public class ConnectedComponentsTest {
    public static void connectedComponents() {
        String directory = "./data/connected-components/";

        for (int c = 1; c <= 4; c++) {
            int cache = 1 << c;
            Cache.setSize(cache);
            Coordinator<Double> coordinator = new Coordinator<Double>(
                    new ScaleFreeNetwork<>(1024, id -> new ConstAgent(0.)),
                    new NetworkStatsLogger(directory + "cache-C" + cache + ".csv", false));
            coordinator.simulate(20);
        }
    }

    public static void main(String[] args) {
        connectedComponents();
    }
}
