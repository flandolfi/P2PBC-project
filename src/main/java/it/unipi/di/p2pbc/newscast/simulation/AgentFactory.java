package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Agent;

public interface AgentFactory<T> {
    Agent<T> createAgent();
}
