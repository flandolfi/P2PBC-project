package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Correspondent;

import java.util.ArrayList;

public class RandomNetworkGenerator<T> extends EmptyNetworkGenerator<T> {
    public RandomNetworkGenerator(int nodes, float probability, AgentFactory<T> agentFactory) {
        super(agentFactory);
        ArrayList<Correspondent<T>> list = new ArrayList<>();

        for (int i = 0; i < nodes; i++) {
            Correspondent<T> peer = addRandomPeer();
            list.add(peer);
        }

        for (int i = 0; i < nodes; i++)
            for (int j = i + 1; j < nodes; j++)
                if (generator.nextFloat() < probability)
                    link(list.get(i), list.get(j));
    }
}
