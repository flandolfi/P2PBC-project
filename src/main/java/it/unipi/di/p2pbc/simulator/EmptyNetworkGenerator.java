package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Correspondent;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;

public class EmptyNetworkGenerator<T> implements NetworkGenerator<T> {
    protected HashSet<Correspondent<T>> network = new HashSet<>();
    protected Random generator = new Random();
    protected AgentFactory<T> agentFactory;

    protected EmptyNetworkGenerator(AgentFactory<T> agentFactory) {
        this.agentFactory = agentFactory;
    }

    protected void link(Correspondent<T> left, Correspondent<T> right) {
        left.getCache().add(right, right.getAgent().getNews());
        right.getCache().add(left, left.getAgent().getNews());
    }

    protected InetSocketAddress generateAddress() {
        InetSocketAddress result = null;
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);

        try {
            result = new InetSocketAddress(InetAddress.getByAddress(bytes), generator.nextInt(65536));
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
    public Set<Correspondent<T>> getNetwork() {
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
                peer.update(list.get(generator.nextInt(list.size())));
            }
        } else {
            Collections.shuffle(list);
            list.subList(0, -delta).forEach(e1 -> list.subList(-delta, list.size())
                    .forEach(e2 -> e2.getCache().removeEntriesFrom(e1)));
            network.removeAll(list.subList(0, -delta));
        }
    }
}
