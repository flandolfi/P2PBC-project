package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Cache;

/**
 * Class representing a lattice network.
 *
 * @param <T> the news data type
 */
public class LatticeNetwork<T> extends EmptyNetwork<T> {

    /**
     * Creates a lattice network. Every added node will have an agent provided by the given {@link
     * AgentFactory}.
     *
     * @param agentFactory the custom {@link AgentFactory}
     * @param size         the size of the network
     */
    public LatticeNetwork(int size, AgentFactory<T> agentFactory) {
        super(agentFactory);

        for (int i = 0; i < size; i++)
            addPeer();

        for (int i = 0; i < size; i++)
            for (int j = 0; j < Cache.getSize(); j++)
                link(network.get(i),
                        network.get((((i + j - Cache.getSize() / 2) % size) + size) % size));
    }
}
