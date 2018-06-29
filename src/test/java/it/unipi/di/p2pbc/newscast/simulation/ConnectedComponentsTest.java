package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Cache;
import it.unipi.di.p2pbc.newscast.core.ConstAgent;
import org.junit.Test;

public class ConnectedComponentsTest {
    @Test
    public void connectedComponents() {
        String directory = "./data/connected-components/";

        for (int c = 1; c <= 4; c++) {
            int cache = 1 << c;
            Cache.setSize(cache);
            Coordinator<Double> coordinator = new Coordinator<>(
                    new ScaleFreeNetwork<>(1024, () -> new ConstAgent(0.)),
                    new NetworkStatsLogger(directory + "cache-C" + cache + ".csv"));
            coordinator.simulate(20);
        }
    }
}
