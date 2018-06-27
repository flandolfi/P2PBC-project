package it.unipi.di.p2pbc.newscast.core;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Cache<T> {
    private class CacheEntry implements Comparable<CacheEntry> {
        final Correspondent<T> peer;
        final Instant timestamp;
        final T data;

        CacheEntry(Correspondent<T> peer, T data) {
            this.peer = peer;
            this.data = data;
            this.timestamp = Instant.now();
        }

        @Override
        public int compareTo(CacheEntry o) {
            return peer.equals(o.peer)? 0 : timestamp.equals(o.timestamp)?
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

    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        Cache.size = size;
    }

    public void add(Correspondent<T> peer, T data) {
        this.add(new CacheEntry(peer, data));
    }

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

    public void removeEntryFrom(Correspondent<T> peer) {
        CacheEntry e = peerToEntry.remove(peer);

        if (e != null)
            cacheEntries.remove(e);
    }

    public void merge(Cache<T> cache) {
        cacheEntries.forEach(cache::add);
        cacheEntries = new TreeSet<>(cache.cacheEntries);
        peerToEntry = new HashMap<>(cache.peerToEntry);

    }

    public Set<Correspondent<T>> getPeers() {
        return new HashSet<>(peerToEntry.keySet());
    }

    public List<T> getNews() {
        return cacheEntries.stream().sorted(Comparator.comparing(l -> l.timestamp))
                .map(e -> e.data).collect(Collectors.toList());
    }
}
