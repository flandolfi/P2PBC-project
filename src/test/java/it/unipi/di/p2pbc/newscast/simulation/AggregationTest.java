package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.AvgAgent;
import it.unipi.di.p2pbc.newscast.core.Cache;
import it.unipi.di.p2pbc.newscast.core.MaxAgent;

import java.util.Random;

public class AggregationTest {
    private static String directory = "./data/aggregation/";

    public static void diffusion() {
        Cache.setSize(40);

        for (int i = 8; i < 15; i++) {
            int size = 1 << i;
            NewsStatsLogger logger = new NewsStatsLogger(
                    directory + "diffusion-N" + size + "-stats.csv",
                    1. / size, 0.001);

            for (int j = 0; j < 5; j++) {
                Coordinator<Double> coordinator = new Coordinator<>(
                        new LatticeNetwork<>(size, id -> new AvgAgent(id > 0 ? 0. : 1.)));
                coordinator.simulate(50);
                coordinator = new Coordinator<>(coordinator.getNetwork(), logger);
                coordinator.simulate(50);
                logger.setCurrentStep(0);
            }
        }
    }

    public static void avg() {
        Random random = new Random();
        int size = 1024;

        for (Integer c : new Integer[]{20, 22, 25, 40, 80}) {
            Cache.setSize(c);
            NewsStatsLogger logger = new NewsStatsLogger(
                    directory + "avg-C" + c + "-stats.csv", 0.);

            for (int i = 0; i < 50; i++) {
                Network<Double> network = new LatticeNetwork<>(size, id -> new AvgAgent(0.));
                Coordinator<Double> coordinator = new Coordinator<>(network);
                coordinator.simulate(50);
                network.getNodes().forEach(n -> n.setAgent(new AvgAgent(random.nextGaussian())));
                coordinator = new Coordinator<>(network, logger);
                coordinator.simulate(50);
                logger.setCurrentStep(0);
            }
        }
    }

    public static void max() {
        int size = 1024;

        for (Integer c : new Integer[]{20, 40, 80}) {
            Cache.setSize(c);
            NewsLogger<Double> logger = new NewsStatsLogger(
                    directory + "max-C" + c + "-stats.csv", 1.);

            for (int i = 0; i < 10; i++) {
                Coordinator<Double> coordinator = new Coordinator<>(
                        new LatticeNetwork<>(size, id -> new MaxAgent(id > 0 ? 0. : 1.)));
                coordinator.simulate(50);
                coordinator = new Coordinator<>(coordinator.getNetwork(), logger);
                coordinator.simulate(12);
                logger.setCurrentStep(0);
            }
        }
    }

    public static void main(String[] args) {
        diffusion();
        avg();
        max();
    }
}
