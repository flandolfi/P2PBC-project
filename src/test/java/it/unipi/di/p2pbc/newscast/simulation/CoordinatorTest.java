package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Agent;
import it.unipi.di.p2pbc.newscast.core.AvgAgent;
import it.unipi.di.p2pbc.newscast.core.Cache;
import org.junit.jupiter.api.Test;

class CoordinatorTest {
    String dataRoot = "./data/";

    @Test
    void simulate() {
        String directory = dataRoot + "simulate-test/";
        Cache.setSize(20);
        int rows = 10;
        int cols = 30;
        Coordinator<Double> coordinator = new Coordinator<>(new GridNetwork<>(rows, cols,
                new AgentFactory<>() {
                    private int value = 1;

                    @Override
                    public Agent<Double> createAgent() {
                        return new AvgAgent(value > 0? (double) value-- : 0.);
                    }
                }),
                new NewsLogger<>(directory + "news.csv"),
                new NetworkStatsLogger(directory + "network-stats.csv"),
                new NewsStatsLogger(directory + "news-stats.csv", 1./(rows*cols)));
        coordinator.simulate(10);
    }
}