package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Agent;

/**
 * This is a dummy interface used by {@link Network}s to assign an {@link Agent}
 * dynamically to each {@link it.unipi.di.p2pbc.newscast.core.Correspondent}
 *
 * @param <T> the news data type used by the {@link Agent}s
 */
public interface AgentFactory<T> {
    Agent<T> createAgent(int iteration);
}
