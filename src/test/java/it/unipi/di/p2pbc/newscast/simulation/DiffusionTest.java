package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Agent;
import it.unipi.di.p2pbc.newscast.core.AvgAgent;
import org.junit.Test;

public class DiffusionTest {
    @Test
    public void diffusion() {
        String directory = "./data/diffusion/";

        for (int i = 8; i < 15; i++) {
            int size = 1 << i;

            Coordinator<Double> coordinator = new Coordinator<>(
                    new ScaleFreeNetwork<>(size, new AgentFactory<>() {
                        double value = 1.;

                        @Override
                        public Agent<Double> createAgent() {
                            return new AvgAgent(value-- > 0.? 1. : 0.);
                        }
                    }),
                    new NewsStatsLogger(directory + "diffusion-N" + size + "-stats.csv", 1./size));
            coordinator.simulate(50);
        }
    }
}
