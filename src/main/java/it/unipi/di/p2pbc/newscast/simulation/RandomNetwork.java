package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.util.ArrayList;

public class RandomNetwork<T> extends EmptyNetwork<T> {
    public RandomNetwork(int nodes, float probability, AgentFactory<T> agentFactory) {
        super(agentFactory);
        ArrayList<Correspondent<T>> list = new ArrayList<>();

        for (int i = 0; i < nodes; i++) {
            Correspondent<T> peer = addRandomPeer();
            list.add(peer);
        }

        for (int i = 0; i < nodes; i++)
            for (int j = i + 1; j < nodes; j++)
                if (random.nextFloat() < probability)
                    link(list.get(i), list.get(j));
    }
}
