package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Cache;
import it.unipi.di.p2pbc.newscast.core.ConstAgent;

public class DegreeDistributionTest {
    public static void randomNetworks() {
        String path = "./data/degree-distributions/random.csv";
        DegreeDistributionLogger logger = new DegreeDistributionLogger(path, false);

        for (int c = 10; c <= 40; c += 10) {
            Cache.setSize(c);
            Network<Double> network = new RandomNetwork<>(80, 0.5, id -> new ConstAgent(0.));
            logger.setCurrentStep(c);
            logger.logNetworkState(network.getNodes());
        }
    }

    public static void scaleFreeNetworks() {
        String path = "./data/degree-distributions/scale-free.csv";
        DegreeDistributionLogger logger = new DegreeDistributionLogger(path, false);

        for (int c = 10; c <= 40; c += 10) {
            Cache.setSize(c);
            Network<Double> network = new ScaleFreeNetwork<>(1 << 14, id -> new ConstAgent(0.));
            logger.setCurrentStep(c);
            logger.logNetworkState(network.getNodes());
        }
    }

    public static void main(String[] args) {
        randomNetworks();
        scaleFreeNetworks();
    }
}
