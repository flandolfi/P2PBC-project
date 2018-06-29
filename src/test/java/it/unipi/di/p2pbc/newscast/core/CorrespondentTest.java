package it.unipi.di.p2pbc.newscast.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorrespondentTest {
    @Test
    void update() {
        Correspondent<Double> peer1 = new Correspondent<>("1", new AvgAgent(1.));
        Correspondent<Double> peer2 = new Correspondent<>("2", new AvgAgent(0.));
        peer1.update(peer2);
        assertTrue(peer1.getPeers().contains(peer2));
        assertTrue(peer2.getPeers().contains(peer1));
        assertEquals(peer1.getAgent().getNews(), peer2.getAgent().getNews());
        assertEquals((double) peer1.getAgent().getNews(), 0.5);
        peer1.update(peer2);
        assertEquals(peer1.getAgent().getNews(), peer2.getAgent().getNews());
        assertEquals((double) peer1.getAgent().getNews(), 0.5);
    }
}