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
        simulate(steps, true);
    }

    public void simulate(int steps, boolean shuffle) {
        List<Correspondent<T>> network = new ArrayList<>(this.network.getNodes());
        Instant lastUpdate = Instant.now();

        if (shuffle)
            Collections.shuffle(network);

        for (int i = 1; i <= steps; i++) {
            System.out.println("NEWSCAST: Simulating step " + i + " of " + steps);

            for (Correspondent<T> node: network) {
                if (node.getLastUpdate().compareTo(lastUpdate) <= 0) {
                    Correspondent<T> peer = node.selectPeer();

                    if (peer != null)
                        node.update(peer);
                }
            }

            loggers.forEach(logger -> logger.logNetworkState(this.network.getNodes()));
            lastUpdate = Instant.now();

            if (Correspondent.passiveUpdates())
                network.sort(Comparator.comparing(Correspondent::getLastUpdate));
        }
    }
}
