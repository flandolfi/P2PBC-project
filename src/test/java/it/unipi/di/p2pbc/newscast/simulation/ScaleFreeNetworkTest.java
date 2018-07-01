package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Cache;
import it.unipi.di.p2pbc.newscast.core.ConstAgent;
import org.junit.jupiter.api.Test;

public class ScaleFreeNetworkTest {
    @Test
    public void scaleFreeNetworks() {
        String path = "./data/scale-free-networks/";
        ConstAgent agent = new ConstAgent(0.);

        for (Integer c : new int[]{20, 40, 80}) {
            Cache.setSize(c);
            NetworkStatsLogger logger = new NetworkStatsLogger(path + "cache-" + c + ".csv");

            for (int exp = 10; exp < 14; exp++) {
                Network<Double> network = new ScaleFreeNetwork<>(1 << exp, () -> agent);
                Coordinator<Double> coordinator = new Coordinator<>(network);
                coordinator.simulate(50);
                logger.setCurrentStep(1 << exp);
                logger.logNetworkState(network.getNodes());
            }
        }
    }

    @Test
    public void randomRemoval() {
        String path = "./data/random-removal/";
        ConstAgent agent = new ConstAgent(0.);
        int size = 10000;

        for (Integer c : new int[]{20, 40, 80}) {
            Cache.setSize(c);
            NetworkStatsLogger logger = new NetworkStatsLogger(path + "cache-" + c + ".csv");
            Network<Double> network = new ScaleFreeNetwork<>(size, () -> agent);
            Coordinator<Double> coordinator = new Coordinator<>(network);
            coordinator.simulate(50);

            for (int p = 90; p <= 100; p++) {
                network.resize((int) (size*(100 - p)/100.));
                logger.setCurrentStep(p);
                logger.logNetworkState(network.getNodes());
            }
        }
    }
}
