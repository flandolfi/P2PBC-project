package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.ConstAgent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NetworkTest {
    @Test
    void resize() {
        EmptyNetwork<Double> network = new EmptyNetwork<>(() -> new ConstAgent(0.));
        network.resize(5);
        assertEquals(5, network.size());
        network.resize(10);
        assertEquals(10, network.size());
        network.resize(1);
        assertEquals(1, network.size());
    }
}