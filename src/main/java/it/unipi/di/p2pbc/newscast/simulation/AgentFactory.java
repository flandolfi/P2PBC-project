package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Agent;

/**
 * This is a dummy interface used by {@link Network}s to assign an {@link Agent} dynamically to each
 * {@link it.unipi.di.p2pbc.newscast.core.Correspondent}.
 *
 * @param <T> the news data type used by the {@link Agent}s
 */
public interface AgentFactory<T> {

    /**
     * Given an iteration or the current size of the network, generates a new {@link Agent}.
     *
     * @param iteration the iteration number
     * @return a new {@link Agent}
     */
    Agent<T> createAgent(int iteration);
}
