package it.unipi.di.p2pbc.newscast.core;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Correspondent<T> {
    private Agent<T> agent;
    private Cache<T> cache;
    private InetSocketAddress address;
    private Instant lastUpdate;
    private static Random random = new Random(42);

    public Correspondent(InetSocketAddress address, Agent<T> agent) {
        this.address = address;
        this.agent = agent;
        this.cache = new Cache<>();
        this.lastUpdate = Instant.now();
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

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void update(Correspondent<T> peer) {
        cache.add(this, agent.getNews());
        peer.cache.add(peer, peer.agent.getNews());
        agent.updateNews(peer.cache.getNews());
        peer.agent.updateNews(cache.getNews());
        cache.merge(peer.cache);
        lastUpdate = Instant.now();
        peer.lastUpdate = lastUpdate; // Comment this line to consider only active updates
    }

    public Set<Correspondent<T>> getPeers() {
        Set<Correspondent<T>> peers = cache.getPeers();
        peers.remove(this);

        return peers;
    }

    public Correspondent<T> selectPeer() {
        ArrayList<Correspondent<T>> peers = new ArrayList<>(getPeers());

        if (peers.size() == 0)
            return null;

        return peers.get(random.nextInt(peers.size()));
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

