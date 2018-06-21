package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Correspondent;

public class ScaleFreeNetworkGenerator<T> extends RandomNetworkGenerator<T> {
    protected ScaleFreeNetworkGenerator(int initialSize, int finalSize, AgentFactory<T> agentFactory) {
        super(initialSize, 1, agentFactory);
        int totalEdges = initialSize*(initialSize - 1)/2;

        for (int i = initialSize; i < finalSize; i++) {
            Correspondent<T> peer = addRandomPeer();

            for (Correspondent<T> node : network)
                if (!node.equals(peer) && generator.nextFloat() < (float) node.getPeers().size()/totalEdges)
                    link(peer, node);

            totalEdges += peer.getPeers().size();
        }
    }
}
