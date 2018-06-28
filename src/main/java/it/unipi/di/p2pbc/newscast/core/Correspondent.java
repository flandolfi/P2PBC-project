package it.unipi.di.p2pbc.newscast.core;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Set;
import java.util.SplittableRandom;

public class Correspondent<T> {
    private Agent<T> agent;
    private Cache<T> cache;
    private String id;
    private Instant lastUpdate;
    private static SplittableRandom random = new SplittableRandom();
    private static boolean countPassiveUpdates = true;

    public static void setPassiveUpdates(boolean countPassiveUpdates) {
        Correspondent.countPassiveUpdates = countPassiveUpdates;
    }

    public static boolean passiveUpdates() {
        return countPassiveUpdates;
    }

    public Correspondent(String id, Agent<T> agent) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    public Cache<T> getCache() {
        return cache;
    }

    public void setId(String id) {
        this.id = id;
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

        if (countPassiveUpdates)
            peer.lastUpdate = lastUpdate;
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
            return id.equals(((Correspondent) obj).id);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

