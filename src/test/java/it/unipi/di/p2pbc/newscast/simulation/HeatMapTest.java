package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.*;
import org.junit.Test;

public class HeatMapTest {
    private String directory = "./data/heatmap/";

    @Test
    public void estimatePi() {
        int radius = 200;
        Coordinator<Double> coordinator = new Coordinator<>(
                new GridNetwork<>(radius, radius, new AgentFactory<>() {
                    int row = 0;
                    int col = 0;

                    @Override
                    public Agent<Double> createAgent() {
                        double value = row*row + col*col > radius*radius? 0. : 1.;
                        col++;

                        if (col > radius) {
                            col = 0;
                            row++;
                        }

                        return new AvgAgent(value);
                    }
                }),
                new NewsLogger<>(directory + "pi-estimation.csv"),
                new NewsStatsLogger(directory + "pi-estimation-stats.csv", Math.PI*0.25));
        coordinator.simulate(100);
    }

    @Test
    public void heatDiffusion() {
        int rows = 200;
        int cols = 100;

        Coordinator<Double> coordinator = new Coordinator<>(
                new GridNetwork<>(rows, cols, new AgentFactory<>() {
                    double value = cols*20.;

                    @Override
                    public Agent<Double> createAgent() {
                        return value-- > 0.? new ConstAgent(1.) : new AvgAgent(0.);
                    }
                }),
                new NewsLogger<>(directory + "heat-diffusion.csv"),
                new NewsStatsLogger(directory + "heat-diffusion-stats.csv", 1.));
        coordinator.simulate(100);
    }

    @Test
    public void sizeEstimation() {
        int rows = 200;
        int cols = 100;

        Coordinator<Double> coordinator = new Coordinator<>(
                new GridNetwork<>(rows, cols, new AgentFactory<>() {
                    double value = 1.;

                    @Override
                    public Agent<Double> createAgent() {
                        return new AvgAgent(value-- > 0.? 1. : 0.);
                    }
                }),
                new NewsLogger<>(directory + "size-estimation.csv"),
                new NewsStatsLogger(directory + "size-estimation-stats.csv", 1./(rows*cols)));
        coordinator.simulate(100);
    }

    @Test
    public void epidemic() {
        int rows = 200;
        int cols = 100;

        Coordinator<Double> coordinator = new Coordinator<>(
                new GridNetwork<>(rows, cols, new AgentFactory<>() {
                    double value = 1.;

                    @Override
                    public Agent<Double> createAgent() {
                        return new MaxAgent(value-- > 0.? 1. : 0.);
                    }
                }),
                new NewsLogger<>(directory + "epidemic.csv"),
                new NewsStatsLogger(directory + "epidemic-stats.csv", 1.));
        coordinator.simulate(100);
    }
}
