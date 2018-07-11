package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.time.Instant;
import java.util.*;

/**
 * This class provides protocol executors, that simulates the behavior of the
 * Neuscast algorithm on a {@link Network} of {@link Correspondent}s.
 *
 * @param <T> the news data type used by the
 *            {@link it.unipi.di.p2pbc.newscast.core.Agent}s
 */
public class Coordinator<T> {
    private Network<T> network;
    private List<Logger<T>> loggers;

    /**
     * Creates a {@link Coordinator} instance that will run on a given
     * {@link Network}, logging statistics on the given {@link Logger}s, if any.
     *
     * @param network the {@link Network} on which the protocol will run
     * @param loggers the {@link Logger}s to be used during the execution of the
     *                algorithm
     */
    public Coordinator(Network<T> network, Logger<T>... loggers) {
        this.network = network;
        this.loggers = Arrays.asList(loggers);
        this.loggers.forEach(logger -> logger.logNetworkState(network.getNodes()));
    }

    /**
     * Returns the {@link Network} on which the protocol is running
     *
     * @return the {@link Network} on which the protocol is running
     */
    public Network<T> getNetwork() {
        return network;
    }

    /**
     * Simulates the protocol for a given number of cycles.
     *
     * @param steps the number of cycles
     */
    public void simulate(int steps) {
        simulate(steps, true);
    }

    /**
     * Simulates the protocol for a given number of cycles. Optionally shuffles
     * the network before starting the simulation.
     *
     * @param steps the number of cycles
     * @param shuffle if {@code true}, randomly permutes the network before the
     *                first cycle of the protocol
     */
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
