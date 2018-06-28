package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

public class RandomNetwork<T> extends EmptyNetwork<T> {
    public RandomNetwork(int nodes, double probability, AgentFactory<T> agentFactory) {
        super(agentFactory);

        for (int i = 0; i < nodes; i++) {
            Correspondent<T> node = addPeer();

            for (Correspondent<T> peer : network)
                if (!node.equals(peer) && random.nextDouble() < probability)
                    link(node, peer);
        }
    }
}
