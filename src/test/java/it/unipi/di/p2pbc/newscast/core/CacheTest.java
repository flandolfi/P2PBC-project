package it.unipi.di.p2pbc.newscast.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {
    @Test
    void add() {
        Cache<Double> cache = new Cache<>();

        for (Integer i = 0; i < 50; i++) {
            cache.add(new Correspondent<>(i.toString(), new ConstAgent(0.)), (double) i);
            assertEquals((double) i, (double) cache.getNews().get(cache.getNews().size() - 1));
        }

        assertEquals(20, cache.getNews().size());

        for (Integer i = 0; i < 50; i++) {
            cache.add(new Correspondent<>(i.toString(), new ConstAgent(0.)), (double) i + 50);
            assertEquals((double) i + 50, (double) cache.getNews().get(cache.getNews().size() - 1));
        }
    }

    @Test
    void removeEntriesFrom() {
        Cache<Double> cache = new Cache<>();
        Correspondent<Double> peer = new Correspondent<>("5", new ConstAgent(0.));

        for (Integer i = 0; i < 20; i++) {
            cache.add(new Correspondent<>(i.toString(), new ConstAgent(0.)), (double) i);
        }

        assertTrue(cache.getPeers().contains(peer));
        cache.removeEntryFrom(peer);
        assertFalse(cache.getPeers().contains(peer));
        cache.removeEntryFrom(peer);
        cache.removeEntryFrom(null);
    }

    @Test
    void merge() {
        Cache<Double> cache = new Cache<>();

        for (Integer i = 0; i < 20; i++) {
            cache.add(new Correspondent<>(i.toString(), new ConstAgent(0.)), (double) i);
        }

        Cache<Double> cache2 = new Cache<>();

        for (Integer i = 20; i < 30; i++) {
            cache2.add(new Correspondent<>(i.toString(), new ConstAgent(0.)), (double) i);
        }

        cache.merge(cache2);

        for (int i = 10; i < 30; i++) {
            assertTrue(cache.getNews().contains((double) i));
        }

        assertArrayEquals(cache.getNews().toArray(), cache2.getNews().toArray());
        Cache<Double> cache3 = new Cache<>();

        for (Integer i = 20; i < 30; i++) {
            cache3.add(new Correspondent<>(i.toString(), new ConstAgent(0.)), (double) (i - 20));
        }

        cache.merge(cache3);

        for (int i = 0; i < 20; i++) {
            assertTrue(cache.getNews().contains((double) i));
        }

        assertArrayEquals(cache.getNews().toArray(), cache3.getNews().toArray());
    }
}