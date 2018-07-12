package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Cache;
import it.unipi.di.p2pbc.newscast.core.ConstAgent;

public class BootstrapTest {
    private static String directory = "./data/bootstrap/";

    public static void lattice() {
        Coordinator<Double> coordinator = new Coordinator<Double>(
                new LatticeNetwork<>(1000, id -> new ConstAgent(0.)),
                new NetworkStatsLogger(directory + "lattice.csv"));
        coordinator.simulate(50);
    }

    public static void random() {
        Coordinator<Double> coordinator = new Coordinator<Double>(
                new RandomNetwork<>(1000, 0.4, id -> new ConstAgent(0.)),
                new NetworkStatsLogger(directory + "random.csv"));
        coordinator.simulate(50);
    }

    public static void scaleFree() {
        Coordinator<Double> coordinator = new Coordinator<Double>(
                new ScaleFreeNetwork<>(1000, id -> new ConstAgent(0.)),
                new NetworkStatsLogger(directory + "scale-free.csv"));
        coordinator.simulate(50);
    }

    public static void grid() {
        Coordinator coordinator = new Coordinator<Double>(
                new GridNetwork<>(10, 100, id -> new ConstAgent(0.)),
                new NetworkStatsLogger(directory + "grid.csv"));
        coordinator.simulate(50);
    }

    public static void growing() {
        Network<Double> network = new EmptyNetwork<>(id -> new ConstAgent(0.));
        network.resize(1);
        Coordinator<Double> coordinator = new Coordinator<Double>(network,
                new NetworkStatsLogger(directory + "growing.csv"));

        for (int i = 50; i <= 1000; i += 50) {
            network.resize(i);
            coordinator.simulate(1, i == 50);
        }

        coordinator.simulate(30, false);
    }

    public static void main(String[] args) {
        Cache.setSize(20);
        lattice();
        random();
        grid();
        growing();
        scaleFree();
    }
}
