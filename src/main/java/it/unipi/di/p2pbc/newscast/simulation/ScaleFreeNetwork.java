package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

public class ScaleFreeNetwork<T> extends RandomNetwork<T> {
    protected ScaleFreeNetwork(int initialSize, int finalSize, AgentFactory<T> agentFactory) {
        super(initialSize, 1, agentFactory);
        int totalEdges = initialSize*(initialSize - 1)/2;

        for (int i = initialSize; i < finalSize; i++) {
            Correspondent<T> peer = addRandomPeer();

            for (Correspondent<T> node : network)
                if (!node.equals(peer) && random.nextFloat() < (float) node.getPeers().size()/totalEdges)
                    link(peer, node);

            totalEdges += peer.getPeers().size();
        }
    }
}
