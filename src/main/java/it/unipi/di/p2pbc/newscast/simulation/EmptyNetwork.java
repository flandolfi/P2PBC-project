package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;

public class EmptyNetwork<T> implements Network<T> {
    protected HashSet<Correspondent<T>> network = new HashSet<>();
    protected Random random = new Random();
    protected AgentFactory<T> agentFactory;

    protected EmptyNetwork(AgentFactory<T> agentFactory) {
        this.agentFactory = agentFactory;
    }

    protected void link(Correspondent<T> left, Correspondent<T> right) {
        left.getCache().add(right, right.getAgent().getNews());
        right.getCache().add(left, left.getAgent().getNews());
    }

    protected InetSocketAddress generateAddress() {
        InetSocketAddress result = null;
        byte[] bytes = new byte[4];
        random.nextBytes(bytes);

        try {
            result = new InetSocketAddress(InetAddress.getByAddress(bytes), random.nextInt(65536));
        } catch (UnknownHostException ignore) {}

        return result;
    }

    public Correspondent<T> addRandomPeer() {
        Correspondent<T> peer;

        do {
            peer = new Correspondent<>(generateAddress(), agentFactory.createAgent());
        } while (!network.add(peer));

        return peer;
    }

    @Override
    public Set<Correspondent<T>> getNodes() {
        return network;
    }

    @Override
    public int size() {
        return network.size();
    }

    @Override
    public void resize(float factor) {
        int delta = (int) (factor - 1.)*network.size();
        ArrayList<Correspondent<T>> list = new ArrayList<>(network);

        if (delta >= 0) {
            for (int i = 0; i < delta; i++) {
                Correspondent<T> peer = addRandomPeer();
                peer.update(list.get(random.nextInt(list.size())));
            }
        } else {
            Collections.shuffle(list);
            list.subList(0, -delta).forEach(e1 -> list.subList(-delta, list.size())
                    .forEach(e2 -> e2.getCache().removeEntryFrom(e1)));
            network.removeAll(list.subList(0, -delta));
        }
    }
}
