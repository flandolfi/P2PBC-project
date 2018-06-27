package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.util.*;

public class EmptyNetwork<T> implements Network<T> {
    protected ArrayList<Correspondent<T>> network = new ArrayList<>();
    protected Random random = new Random();
    protected AgentFactory<T> agentFactory;
    protected Integer nodeId = 0;

    protected EmptyNetwork(AgentFactory<T> agentFactory) {
        this.agentFactory = agentFactory;
    }

    protected void link(Correspondent<T> left, Correspondent<T> right) {
        left.getCache().add(right, right.getAgent().getNews());
        right.getCache().add(left, left.getAgent().getNews());
    }

    protected Correspondent<T> addPeer() {
        Correspondent<T> peer = new Correspondent<>((nodeId++).toString(), agentFactory.createAgent());
        network.add(peer);

        return peer;
    }

    @Override
    public List<Correspondent<T>> getNodes() {
        return network;
    }

    @Override
    public int size() {
        return network.size();
    }

    @Override
    public void resize(double factor) {
        int delta = (int) ((factor - 1.)*network.size());

        if (delta >= 0) {
            for (int i = 0; i < delta; i++)
                addPeer().update(network.get(random.nextInt(network.size())));
        } else {
            Collections.shuffle(network);
            network.subList(0, -delta).forEach(e1 -> network.subList(-delta, network.size())
                    .forEach(e2 -> e2.getCache().removeEntryFrom(e1)));
            network.removeAll(network.subList(0, -delta));
        }
    }
}
