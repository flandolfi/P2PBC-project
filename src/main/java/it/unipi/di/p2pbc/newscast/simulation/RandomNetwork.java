package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

/**
 * Class representing a random network.
 *
 * @param <T> the news data type
 */
public class RandomNetwork<T> extends EmptyNetwork<T> {

    /**
     * Creates an random network, using the Erdős–Rényi model G(N, p).
     * Notice that, due to the limited cache size, the actual network topology
     * may not be consistent with the expected one. Every added node will have
     * an agent provided by he given {@link AgentFactory}.
     *
     * @param agentFactory the custom {@link AgentFactory}
     * @param nodes the number of nodes in the network
     * @param probability the probability that a node will be connected to
     *                    another <b>at creation time</b> (the final probability
     *                    will be dependent on the cache size)
     */
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
