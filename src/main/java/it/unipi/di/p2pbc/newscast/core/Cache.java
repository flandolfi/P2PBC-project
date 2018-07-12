package it.unipi.di.p2pbc.newscast.core;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The cache stored by a {@link Correspondent}.
 *
 * @param <T> the type of values stored by the cache. It should coincide by the type of values used
 *            by the {@link Agent}
 */
public class Cache<T> {

    // Entries of the cache
    private class CacheEntry implements Comparable<CacheEntry> {
        final Correspondent<T> peer;
        final Instant timestamp;
        final T data;

        CacheEntry(Correspondent<T> peer, T data) {
            this.peer = peer;
            this.data = data;
            this.timestamp = Instant.now();
        }

        // CompareTo() & equals() method used by the TreeSet for maintaining the cache entries
        // sorted by timestamp. Two entries are equal if and only if the Correspondent is the same
        // (it is required that there are no more than 1 entry for a given peer). Ties on the
        // timestamps are broken arbitrarily.
        @Override
        public int compareTo(CacheEntry o) {
            return peer.equals(o.peer) ? 0 : timestamp.equals(o.timestamp) ?
                    peer.getId().compareTo(o.peer.getId()) : timestamp.compareTo(o.timestamp);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Cache.CacheEntry) {
                return peer.equals(((Cache.CacheEntry) o).peer);
            }

            return false;
        }
    }

    private TreeSet<CacheEntry> cacheEntries = new TreeSet<>();
    private HashMap<Correspondent<T>, CacheEntry> peerToEntry = new HashMap<>();
    private static int size = 20;

    /**
     * Returns the maximum capacity of the caches.
     *
     * @return the maximum capacity of the caches
     */
    public static int getSize() {
        return size;
    }

    /**
     * Sets the maximum capacity of the caches.
     *
     * @param size the new size
     */
    public static void setSize(int size) {
        Cache.size = size;
    }

    /**
     * Adds a new entry to the cache. The generated entry will have a timestamp set to {@link
     * Instant#now()}.
     *
     * @param peer the {@link Correspondent} that provided the news
     * @param data the news information
     */
    public void add(Correspondent<T> peer, T data) {
        this.add(new CacheEntry(peer, data));
    }

    // Used to add entries already created
    private void add(CacheEntry entry) {
        CacheEntry e = peerToEntry.get(entry.peer);

        if (e != null) {
            if (entry.timestamp.compareTo(e.timestamp) > 0)
                cacheEntries.remove(e);
            else
                return;
        }

        cacheEntries.add(entry);
        peerToEntry.put(entry.peer, entry);

        if (cacheEntries.size() > size) {
            peerToEntry.remove(cacheEntries.pollFirst().peer);
        }
    }

    /**
     * Removes the entry of a given peer.
     *
     * @param peer the {@link Correspondent} of the entry to be removed
     */
    public void removeEntryFrom(Correspondent<T> peer) {
        CacheEntry e = peerToEntry.remove(peer);

        if (e != null)
            cacheEntries.remove(e);
    }

    /**
     * Merges the current cache with a given one, adding all of its elements and then removing the
     * oldest ones.
     *
     * @param cache a {@link Cache} object
     */
    public void merge(Cache<T> cache) {
        cacheEntries.forEach(cache::add);
        cacheEntries = new TreeSet<>(cache.cacheEntries);
        peerToEntry = new HashMap<>(cache.peerToEntry);

    }

    /**
     * Returns the {@link Set} of {@link Correspondent}s present in the cache entries.
     *
     * @return a {@link Set} of {@link Correspondent}s
     */
    public Set<Correspondent<T>> getPeers() {
        return new HashSet<>(peerToEntry.keySet());
    }

    /**
     * Returns the {@link List} of news present in the cache entries, sorted by timestamps (from the
     * oldest to the newest).
     *
     * @return a {@link List} of news of {@link T} type
     */
    public List<T> getNews() {
        return cacheEntries.stream().sorted(Comparator.comparing(l -> l.timestamp))
                .map(e -> e.data).collect(Collectors.toList());
    }
}
