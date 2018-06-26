package it.unipi.di.p2pbc.newscast.core;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Cache<T> {
    private class CacheEntry implements Comparable<CacheEntry> {
        Correspondent<T> peer;
        Instant timestamp;
        T data;

        CacheEntry(Correspondent<T> peer, T data) {
            this.peer = peer;
            this.data = data;
            this.timestamp = Instant.now();
        }

        @Override
        public int compareTo(CacheEntry o) {
            return peer.equals(o.peer)? 0 : timestamp.equals(o.timestamp)? 1 : timestamp.compareTo(o.timestamp);
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
        cacheEntries.removeIf(e -> e.peer == entry.peer && e.timestamp.compareTo(entry.timestamp) < 0);
        cacheEntries.add(entry);

        if (cacheEntries.size() > size) {
            cacheEntries.pollFirst();
        }
    }

    public void removeEntriesFrom(Correspondent<T> peer) {
        cacheEntries.removeIf(e -> e.peer.equals(peer));
    }

    public void merge(Cache<T> cache) {
        cacheEntries.forEach(cache::add);
        cacheEntries = new TreeSet<>(cache.cacheEntries);
    }

    public Set<Correspondent<T>> getPeers() {
        return cacheEntries.stream().map(e -> e.peer)
                .collect(Collectors.toSet());
    }

    public List<T> getNews() {
        return cacheEntries.stream().map(e -> e.data)
                .collect(Collectors.toList());
    }
}
