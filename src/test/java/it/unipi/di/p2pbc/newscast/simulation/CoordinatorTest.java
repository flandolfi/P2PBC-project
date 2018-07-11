package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.AvgAgent;
import it.unipi.di.p2pbc.newscast.core.Cache;
import org.junit.jupiter.api.Test;

class CoordinatorTest {
    private String directory = "./data/simulate-test/";

    @Test
    void simulate() {
        Cache.setSize(20);
        int rows = 10;
        int cols = 30;
        Coordinator<Double> coordinator = new Coordinator<>(new GridNetwork<>(rows, cols, id -> new AvgAgent(id > 0? 0. : 1.)),
                new NewsLogger<>(directory + "news.csv"),
                new NetworkStatsLogger(directory + "network-stats.csv"),
                new NewsStatsLogger(directory + "news-stats.csv", 1./(rows*cols)));
        coordinator.simulate(10);
    }
}