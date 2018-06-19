package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Correspondent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Coordinator<T> {
    private NetworkGenerator<T> generator;

    public Coordinator(NetworkGenerator<T> generator) {
        this.generator = generator;
    }

    public void simulate(int size, int steps, Logger<Correspondent<T>> logger) {
        List<Correspondent<T>> network = generator.instantiateNetwork(size);
        HashMap<Correspondent<T>, Integer> lastUpdate = new HashMap<>();
        network.forEach(n -> lastUpdate.put(n, -1));
        logger.beginSession(size);

        for (int i = 0; i < steps; i++) {
            Collections.shuffle(network);
            logger.logNetworkState(network, i);

            for (Correspondent<T> node: network) {
                if (lastUpdate.get(node) == i)
                    continue;

                Correspondent<T> peer = node.getRandomPeer();
                lastUpdate.put(node, i);
                lastUpdate.put(peer, i);

                node.update(peer);
            }
        }

        logger.logNetworkState(network, steps);
        logger.endSession();
    }
}
