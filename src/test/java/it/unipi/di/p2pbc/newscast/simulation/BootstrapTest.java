package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Cache;
import it.unipi.di.p2pbc.newscast.core.ConstAgent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BootstrapTest {
    private String directory = "./data/bootstrap/";

    @BeforeAll
    static void init() {
        Cache.setSize(20);
    }

    @Test
    void lattice() {
        Coordinator<Double> coordinator = new Coordinator<>(
                new LatticeNetwork<>(1000, id -> new ConstAgent(0.)),
                new NetworkStatsLogger(directory + "lattice.csv"));
        coordinator.simulate(50);
    }

    @Test
    void random() {
        Coordinator<Double> coordinator = new Coordinator<>(
                new RandomNetwork<>(1000, 0.4, id -> new ConstAgent(0.)),
                new NetworkStatsLogger(directory + "random.csv"));
        coordinator.simulate(50);
    }

    @Test
    void scaleFree() {
        Coordinator<Double> coordinator = new Coordinator<>(
                new ScaleFreeNetwork<>(1000, id -> new ConstAgent(0.)),
                new NetworkStatsLogger(directory + "scale-free.csv"));
        coordinator.simulate(50);
    }

    @Test
    void grid() {
        Coordinator<Double> coordinator = new Coordinator<>(
                new GridNetwork<>(10, 100, id -> new ConstAgent(0.)),
                new NetworkStatsLogger(directory + "grid.csv"));
        coordinator.simulate(50);
    }

    @Test
    void growing() {
        Network<Double> network = new EmptyNetwork<>(id -> new ConstAgent(0.));
        network.resize(1);
        Coordinator<Double> coordinator = new Coordinator<>(network,
                new NetworkStatsLogger(directory + "growing.csv"));

        for (int i = 50; i <= 1000; i += 50) {
            network.resize(i);
            coordinator.simulate(1, i == 50);
        }

        coordinator.simulate(30, false);
    }
}
