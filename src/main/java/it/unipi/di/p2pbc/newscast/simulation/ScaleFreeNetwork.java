package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

public class ScaleFreeNetwork<T> extends RandomNetwork<T> {
    public ScaleFreeNetwork(int size, AgentFactory<T> agentFactory) {
        this(2, size, agentFactory);
    }

    public ScaleFreeNetwork(int initialSize, int finalSize, AgentFactory<T> agentFactory) {
        super(initialSize, 1., agentFactory);

        if (initialSize < 2)
            throw new IllegalArgumentException("Initial size must be at least 2");

        int totalEdges = initialSize*(initialSize - 1);

        for (int i = initialSize; i < finalSize; i++) {
            Correspondent<T> peer = addPeer();

            while (peer.getPeers().size() == 0)
                for (Correspondent<T> node : network)
                    if (!node.equals(peer) && random.nextDouble() < (double) node.getPeers().size()/totalEdges)
                        link(peer, node);

            totalEdges += peer.getPeers().size()*2;
        }
    }
}
