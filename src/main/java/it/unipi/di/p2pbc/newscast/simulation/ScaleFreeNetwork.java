package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

public class ScaleFreeNetwork<T> extends RandomNetwork<T> {
    public ScaleFreeNetwork(int size, AgentFactory<T> agentFactory) {
        this(2, size, true,  agentFactory);
    }

    public ScaleFreeNetwork(int initialSize, int finalSize, AgentFactory<T> agentFactory) {
        this(initialSize, finalSize, true, agentFactory);
    }

    public ScaleFreeNetwork(int size, boolean redraw, AgentFactory<T> agentFactory) {
        this(2, size, redraw,  agentFactory);
    }

    public ScaleFreeNetwork(int initialSize, int finalSize, boolean redraw, AgentFactory<T> agentFactory) {
        super(initialSize, 1., agentFactory);

        if (initialSize < 2)
            throw new IllegalArgumentException("Initial size must be at least 2");

        int totalEdges = initialSize*(initialSize - 1);

        for (int i = initialSize; i < finalSize; i++) {
            Correspondent<T> peer = addPeer();

            do {
                for (Correspondent<T> node : network)
                    if (!node.equals(peer) && random.nextDouble() < (double) node.getPeers().size() / totalEdges)
                        link(peer, node);
            } while (redraw && peer.getPeers().size() == 0);

            totalEdges += peer.getPeers().size()*2;
        }
    }
}
