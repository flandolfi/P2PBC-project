package it.unipi.di.p2pbc.newscast.simulation;

public class RandomNetwork<T> extends EmptyNetwork<T> {
    public RandomNetwork(int nodes, float probability, AgentFactory<T> agentFactory) {
        super(agentFactory);

        for (int i = 0; i < nodes; i++)
            addPeer();

        for (int i = 0; i < nodes; i++)
            for (int j = i + 1; j < nodes; j++)
                if (random.nextFloat() < probability)
                    link(network.get(i), network.get(j));
    }
}
