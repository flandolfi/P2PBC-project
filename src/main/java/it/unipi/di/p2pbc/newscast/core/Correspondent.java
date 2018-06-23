package it.unipi.di.p2pbc.newscast.core;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Set;

public class Correspondent<T> {
    private Agent<T> agent;
    private Cache<T> cache;
    private InetSocketAddress address;
    private static Random generator = new Random(42);

    public Correspondent(InetSocketAddress address, Agent<T> agent) {
        this.address = address;
        this.agent = agent;
        this.cache = new Cache<>();
    }

    public Agent<T> getAgent() {
        return agent;
    }

    public void setAgent(Agent<T> agent) {
        this.agent = agent;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public Cache<T> getCache() {
        return cache;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public void update(Correspondent<T> peer) {
        T news = agent.getNews();
        cache.add(this, news);
        agent.updateNews(peer.cache.getNews());
        cache.merge(peer.cache);
    }

    public Set<Correspondent<T>> getPeers() {
        Set<Correspondent<T>> peers = cache.getPeers();
        peers.remove(this);

        return peers;
    }

    public Correspondent<T> selectPeer() {
        Set<Correspondent<T>> peers = getPeers();

        return (Correspondent<T>) peers.toArray()[generator.nextInt(peers.size())];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Correspondent) {
            return address.equals(((Correspondent) obj).address);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}

