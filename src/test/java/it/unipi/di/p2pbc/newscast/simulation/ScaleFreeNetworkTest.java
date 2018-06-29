package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Cache;
import it.unipi.di.p2pbc.newscast.core.ConstAgent;
import org.junit.Test;

public class ScaleFreeNetworkTest {
    @Test
    public void scaleFreeNetworks() {
        String path = "./data/scale-free-networks/";
        ConstAgent agent = new ConstAgent(0.);

        for (Integer c : new int[]{20, 40, 80}) {
            Cache.setSize(c);
            Logger<Double> logger = new NetworkStatsLogger(path + "cache-" + c + ".csv");

            for (int i = 10; i < 14; i++) {
                System.out.println(" -- CACHE SIZE: " + c + ", NETWORK SIZE: " + (1 << i));
                Network<Double> network = new ScaleFreeNetwork<>(1 << i, () -> agent);
                Coordinator<Double> coordinator = new Coordinator<>(network);
                coordinator.simulate(50);
                logger.logNetworkState(network.getNodes());
            }
        }
    }

    @Test
    public void randomRemoval() {
        String path = "./data/random-removal/";
        ConstAgent agent = new ConstAgent(0.);
        int size = 1 << 14;

        for (Integer c : new int[]{20, 40, 80}) {
            Cache.setSize(c);
            Logger<Double> logger = new NetworkStatsLogger(path + "cache-" + c + ".csv");
            Network<Double> network = new ScaleFreeNetwork<>(size, () -> agent);
            Coordinator<Double> coordinator = new Coordinator<>(network);
            coordinator.simulate(50);

            for (int factor = 90; factor <= 100; factor++) {
                System.out.println(" -- CACHE SIZE: " + c + ", FACTOR: " + factor);
                network.resize((int) (size*(100 - factor)/100.));
                logger.logNetworkState(network.getNodes());
            }
        }
    }
}
