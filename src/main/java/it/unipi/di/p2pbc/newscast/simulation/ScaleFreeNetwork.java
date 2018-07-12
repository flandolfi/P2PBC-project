package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

/**
 * Class representing an scale-free network.
 *
 * @param <T> the news data type
 */
public class ScaleFreeNetwork<T> extends RandomNetwork<T> {

    /**
     * Constructs a scale-free network using the Barabási–Albert algorithm, starting from a network
     * of size 2. If a node does not connect to any other node, it will be redrawn until it is
     * connected. Notice that, due to the limited cache size, the actual network topology may not be
     * consistent with the expected one. Every added node will have an agent provided by he given
     * {@link AgentFactory}.
     *
     * @param size         the size of the network
     * @param agentFactory the custom {@link AgentFactory}
     */
    public ScaleFreeNetwork(int size, AgentFactory<T> agentFactory) {
        this(2, size, true, agentFactory);
    }

    /**
     * Constructs a scale-free network using the Barabási–Albert algorithm, starting from a network
     * of size {@code initialSize}. If a node does not connect to any other node, it will be redrawn
     * until it is connected. Notice that, due to the limited cache size, the actual network
     * topology may not be consistent with the expected one. Every added node will have an agent
     * provided by he given {@link AgentFactory}.
     *
     * @param finalSize    the size of the network
     * @param initialSize  the size of the initial fully connected network
     * @param agentFactory the custom {@link AgentFactory}
     */
    public ScaleFreeNetwork(int initialSize, int finalSize, AgentFactory<T> agentFactory) {
        this(initialSize, finalSize, true, agentFactory);
    }

    /**
     * Constructs a scale-free network using the Barabási–Albert algorithm, starting from a network
     * of size 2. If {@code redraw} is {@code true} and a node does not connect to any other node,
     * it will be redrawn until it is connected. Notice that, due to the limited cache size, the
     * actual network topology may not be consistent with the expected one. Every added node will
     * have an agent provided by he given {@link AgentFactory}.
     *
     * @param size         the size of the network
     * @param redraw       if {@code true} nd a node does not connect to any other node, it will be
     *                     redrawn until it is connected. In any case, the algorithm may generate
     *                     disconnected networks
     * @param agentFactory the custom {@link AgentFactory}
     */
    public ScaleFreeNetwork(int size, boolean redraw, AgentFactory<T> agentFactory) {
        this(2, size, redraw, agentFactory);
    }

    /**
     * Constructs a scale-free network using the Barabási–Albert algorithm, starting from a network
     * of size {@code initialSize}. If {@code redraw} is {@code true} and a node does not connect to
     * any other node, it will be redrawn until it is connected. Notice that, due to the limited
     * cache size, the actual network topology may not be consistent with the expected one. Every
     * added node will have an agent provided by he given {@link AgentFactory}.
     *
     * @param finalSize    the size of the network
     * @param initialSize  the size of the initial network
     * @param redraw       if {@code true} nd a node does not connect to any other node, it will be
     *                     redrawn until it is connected. In any case, the algorithm may generate
     *                     disconnected networks
     * @param agentFactory the custom {@link AgentFactory}
     */
    public ScaleFreeNetwork(int initialSize, int finalSize, boolean redraw,
                            AgentFactory<T> agentFactory) {
        super(initialSize, 1., agentFactory);

        if (initialSize < 2)
            throw new IllegalArgumentException("Initial size must be at least 2");

        int totalEdges = initialSize * (initialSize - 1);

        for (int i = initialSize; i < finalSize; i++) {
            Correspondent<T> peer = addPeer();

            do {
                for (Correspondent<T> node : network)
                    if (!node.equals(peer)
                            && random.nextDouble() < (double) node.getPeers().size() / totalEdges)
                        link(peer, node);
            } while (redraw && peer.getPeers().size() == 0);

            totalEdges += peer.getPeers().size() * 2;
        }
    }
}
