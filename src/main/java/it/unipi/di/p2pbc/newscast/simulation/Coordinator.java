package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.time.Instant;
import java.util.*;

public class Coordinator<T> {
    private Network<T> network;
    private int currentStep;
    private Logger<T> logger;


    public Coordinator(Network<T> network, Logger<T> logger) {
        this.network = network;
        this.currentStep = 0;
        this.logger = logger;
    }

    public void simulate(int steps) {
        simulate(steps, false);
    }

    public void simulate(int steps, boolean shuffle) {
        List<Correspondent<T>> network = new ArrayList<>(this.network.getNodes());
        Instant lastUpdate = Instant.now();

        if (shuffle)
            Collections.shuffle(network);

        for (; currentStep < currentStep + steps; currentStep++) {
            logger.logNetworkState(network, currentStep);

            for (Correspondent<T> node: network) {
                if (node.getLastUpdate().compareTo(lastUpdate) <= 0) {
                    Correspondent<T> peer = node.selectPeer();

                    if (peer != null)
                        node.update(peer);
                }
            }

            network.sort(Comparator.comparing(Correspondent::getLastUpdate));
            lastUpdate = Instant.now();
        }

        logger.logNetworkState(network, steps);
    }
}
