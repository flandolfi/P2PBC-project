package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.*;
import org.junit.jupiter.api.Test;

public class HeatMapTest {
    private String directory = "./data/heatmap/";

    @Test
    public void estimatePi() {
        int radius = 100;
        Coordinator<Double> coordinator = new Coordinator<>(
                new GridNetwork<>(radius, radius, iteration -> {
                    int row = iteration / radius;
                    int col = iteration % radius;
                    double value = row*row + col*col > (radius - 1)*(radius - 1)? 0. : 1.;

                    return new AvgAgent(value);
                }),
                new NewsLogger<>(directory + "pi-estimation.csv"),
                new NewsStatsLogger(directory + "pi-estimation-stats.csv", Math.PI*0.25));
        coordinator.simulate(100);
    }

    @Test
    public void sizeEstimation() {
        int rows = 100;
        int cols = 100;

        Coordinator<Double> coordinator = new Coordinator<>(
                new GridNetwork<>(rows, cols, id -> new AvgAgent(id > 0? 0. : 1.)),
                new NewsLogger<>(directory + "size-estimation.csv"),
                new NewsStatsLogger(directory + "size-estimation-stats.csv", 1./(rows*cols)));
        coordinator.simulate(100);
    }

    @Test
    public void epidemic() {
        int rows = 100;
        int cols = 100;

        Coordinator<Double> coordinator = new Coordinator<>(
                new GridNetwork<>(rows, cols, id -> new MaxAgent(id > 0? 0. : 1.)),
                new NewsLogger<>(directory + "epidemic.csv"),
                new NewsStatsLogger(directory + "epidemic-stats.csv", 1.));
        coordinator.simulate(100);
    }
}
