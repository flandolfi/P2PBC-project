package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Correspondent;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;

public class RandomNetworkGenerator<T> implements NetworkGenerator {
    protected HashSet<Correspondent<T>> network = new HashSet<>();
    protected Random generator = new Random();
    protected AgentFactory<T> agentFactory;

    protected RandomNetworkGenerator(AgentFactory<T> agentFactory) {
        this.agentFactory = agentFactory;
    }

    public RandomNetworkGenerator(int nodes, int edges, AgentFactory<T> agentFactory) {
        this(agentFactory);
        ArrayList<Correspondent<T>> list = new ArrayList<>();

        for (int i = 0; i < nodes; i++) {
            Correspondent<T> peer = addRandomPeer();
            list.add(peer);
        }

        for (int i = 0; i < edges; i++) {
            Correspondent<T> edgeLeft = list.get(generator.nextInt(list.size()));
            Correspondent<T> edgeRight = list.get(generator.nextInt(list.size()));
            edgeLeft.getCache().add(edgeRight, edgeRight.getAgent().getNews());
            edgeRight.getCache().add(edgeLeft, edgeLeft.getAgent().getNews());
        }
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

    protected Correspondent<T> addRandomPeer() {
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
