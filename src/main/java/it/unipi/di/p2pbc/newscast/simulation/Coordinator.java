package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.time.Instant;
import java.util.*;

public class Coordinator<T> {
    private Network<T> network;
    private List<Logger<T>> loggers;


    public Coordinator(Network<T> network, Logger<T>... loggers) {
        this.network = network;
        this.loggers = Arrays.asList(loggers);
        this.loggers.forEach(logger -> logger.logNetworkState(network.getNodes()));
    }

    public void simulate(int steps) {
        simulate(steps, false);
    }

    public void simulate(int steps, boolean shuffle) {
        List<Correspondent<T>> network = new ArrayList<>(this.network.getNodes());
        Instant lastUpdate = Instant.now();

        if (shuffle)
            Collections.shuffle(network);
        else
            network.sort(Comparator.comparing(Correspondent::getLastUpdate));

        for (int i = 0; i < steps; i++) {
            for (Correspondent<T> node: network) {
                if (node.getLastUpdate().compareTo(lastUpdate) <= 0) {
                    Correspondent<T> peer = node.selectPeer();

                    if (peer != null)
                        node.update(peer);
                }
            }

            loggers.forEach(logger -> logger.logNetworkState(network));
            network.sort(Comparator.comparing(Correspondent::getLastUpdate));
            lastUpdate = Instant.now();
        }
    }
}
