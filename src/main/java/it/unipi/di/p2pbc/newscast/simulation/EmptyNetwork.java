package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.util.*;

/**
 * Class representing an empty network.
 *
 * @param <T> the news data type
 */
public class EmptyNetwork<T> implements Network<T> {
    protected ArrayList<Correspondent<T>> network = new ArrayList<>();
    protected SplittableRandom random = new SplittableRandom();
    protected AgentFactory<T> agentFactory;
    protected Integer nodeId = 0;

    /**
     * Creates an empty network. Every added node will have an agent provided by
     * the given {@link AgentFactory}.
     *
     * @param agentFactory the custom {@link AgentFactory}
     */
    public EmptyNetwork(AgentFactory<T> agentFactory) {
        this.agentFactory = agentFactory;
    }

    protected void link(Correspondent<T> left, Correspondent<T> right) {
        left.getCache().add(right, right.getAgent().getNews());
        right.getCache().add(left, left.getAgent().getNews());
    }

    protected Correspondent<T> addPeer() {
        Correspondent<T> peer = new Correspondent<>((nodeId).toString(), agentFactory.createAgent(nodeId));
        network.add(peer);
        nodeId++;

        return peer;
    }

    /**
     * Returns the {@link Correspondent}s in the network.
     *
     * @return a {@link List} of {@link Correspondent}s
     */
    @Override
    public List<Correspondent<T>> getNodes() {
        return network;
    }

    /**
     * Returns the current size of the network.
     *
     * @return the current size of the network
     */
    @Override
    public int size() {
        return network.size();
    }

    /**
     * Resizes the network to a given size. If the new size is smaller than the
     * actual size, randomly removes nodes from the network. Otherwise, link new
     * nodes to the network performing an update to a random node in the
     * network.
     *
     * @param size the new size of the network
     */
    @Override
    public void resize(int size) {
        int delta = size - network.size();

        if (delta >= 0) {
            for (int i = 0; i < delta; i++) {
                Correspondent<T> peer = addPeer();

                if (network.size() > 1)
                    peer.update(network.get(random.nextInt(network.size() - 1)));
            }
        } else {
            Collections.shuffle(network);
            network.subList(0, -delta).forEach(e1 -> network.subList(-delta, network.size())
                    .forEach(e2 -> e2.getCache().removeEntryFrom(e1)));
            network.removeAll(network.subList(0, -delta));
        }
    }
}
