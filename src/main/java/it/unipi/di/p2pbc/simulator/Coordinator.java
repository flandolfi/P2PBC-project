package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Correspondent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Coordinator<T> {
    private NetworkGenerator<T> generator;
    private int currentStep;
    private Logger<T> logger;


    public Coordinator(NetworkGenerator<T> generator, Logger<T> logger) {
        this.generator = generator;
        this.currentStep = 0;
        this.logger = logger;
    }

    public void simulate(int steps) {
        List<Correspondent<T>> network = new ArrayList<>(generator.getNetwork());
        HashMap<Correspondent, Integer> lastUpdate = new HashMap<>();
        generator.getNetwork().forEach(n -> lastUpdate.put(n, currentStep - 1));

        for (; currentStep < currentStep + steps; currentStep++) {
            logger.logNetworkState(network, currentStep);

            for (Correspondent<T> node: network) {
                if (lastUpdate.get(node) == currentStep)
                    continue;

                Correspondent peer = node.getRandomPeer();
                lastUpdate.put(node, currentStep);
                lastUpdate.put(peer, currentStep);

                node.update(peer);
            }

            Collections.shuffle(network);
        }

        logger.logNetworkState(network, steps);
    }
}
