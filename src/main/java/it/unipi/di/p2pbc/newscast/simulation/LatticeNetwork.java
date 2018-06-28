package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Cache;

public class LatticeNetwork<T> extends EmptyNetwork<T> {
    public LatticeNetwork(int size, AgentFactory<T> agentFactory) {
        super(agentFactory);

        for (int i = 0; i < size; i++)
            addPeer();

        for (int i = 0; i < size; i++)
            for (int j = 0; j < Cache.getSize(); j++)
                link(network.get(i), network.get((((i + j - Cache.getSize()/2) % size) + size) % size));
    }
}
