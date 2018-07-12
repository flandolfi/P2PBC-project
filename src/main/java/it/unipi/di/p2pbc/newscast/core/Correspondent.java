package it.unipi.di.p2pbc.newscast.core;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Set;
import java.util.SplittableRandom;

/**
 * This class represents a peer in the network, constantly exchanging news with an {@link Agent}
 * instance.
 *
 * @param <T> the data type of the news used by the {@link Agent}
 */
public class Correspondent<T> {
    private Agent<T> agent;
    private Cache<T> cache;
    private String id;
    private Instant lastUpdate;
    private static SplittableRandom random = new SplittableRandom();
    private static boolean countPassiveUpdates = false;

    /**
     * Sets the timer update criterion. If {@code countPassiveUpdates} is {@code true}, the internal
     * timer of each {@link Correspondent} is started whenever it updates its {@link Agent}'s news
     * (the update might be active or passive). If {@code false} (default value), the internal timer
     * of each {@link Correspondent} is started only on active updates.
     *
     * @param countPassiveUpdates the new timer update criteirion
     */
    public static void setPassiveUpdates(boolean countPassiveUpdates) {
        Correspondent.countPassiveUpdates = countPassiveUpdates;
    }

    /**
     * Returns the current timer update criterion.
     *
     * @return the current timer update criterion
     * @see Correspondent#setPassiveUpdates
     */
    public static boolean passiveUpdates() {
        return countPassiveUpdates;
    }

    /**
     * Creates a {@link Correspondent} with identifier {@code id} and {@link Agent} agent.
     *
     * @param id    the {@link Correspondent} identifier
     * @param agent the {@link Agent} with which it will exchange news
     */
    public Correspondent(String id, Agent<T> agent) {
        this.id = id;
        this.agent = agent;
        this.cache = new Cache<>();
        this.lastUpdate = Instant.now();
    }

    /**
     * Returns the current {@link Agent}.
     *
     * @return the current {@link Agent}
     */
    public Agent<T> getAgent() {
        return agent;
    }

    /**
     * Sets the {@link Agent} of the {@link Correspondent}.
     *
     * @return the new {@link Agent}
     */
    public void setAgent(Agent<T> agent) {
        this.agent = agent;
    }

    /**
     * Returns the identifier of the {@link Correspondent}.
     *
     * @return the {@link String} identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the identifier of the {@link Correspondent}.
     *
     * @return the new {@link String} identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the {@link Cache} of the {@link Correspondent}.
     *
     * @return the current {@link Cache}
     */
    public Cache<T> getCache() {
        return cache;
    }

    /**
     * Returns the {@link Instant} in which the {@link Correspondent} last updated the news.
     *
     * @return the last update {@link Instant}
     */
    public Instant getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Updates the {@link Cache} and the {@link Agent}'s news exchangeing them with the passed
     * {@link Correspondent}.
     *
     * @param peer the {@link Correspondent} with which it will exchange news
     */
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

    /**
     * Returns the known {@link Correspondent}s present in {@link Cache}, excluded the {@link
     * Correspondent} itself.
     *
     * @return a {@link Set} of {@link Correspondent}
     */
    public Set<Correspondent<T>> getPeers() {
        Set<Correspondent<T>> peers = cache.getPeers();
        peers.remove(this);

        return peers;
    }

    /**
     * Selects a random peer from the known {@link Correspondent}s.
     *
     * @return a {@link Correspondent}
     * @see Correspondent#getPeers()
     */
    public Correspondent<T> selectPeer() {
        ArrayList<Correspondent<T>> peers = new ArrayList<>(getPeers());

        if (peers.size() == 0)
            return null;

        return peers.get(random.nextInt(peers.size()));
    }

    /**
     * Compares the specified object with this {@link Correspondent}. It returns {@code true} only
     * if the object is a {@link Correspondent} instance and that have the same {@code id}.
     *
     * @param obj the object to compare
     * @return {@code true} if {@code obj} is a {@link Correspondent} and {@code this.getId() ==
     * obj.getId()}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Correspondent) {
            return id.equals(((Correspondent) obj).id);
        }

        return false;
    }

    /**
     * Returns the hashcode of the {@link Correspondent}.
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

