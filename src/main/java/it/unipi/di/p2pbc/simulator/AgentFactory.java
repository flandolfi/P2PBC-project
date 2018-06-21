package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Agent;

public interface AgentFactory<T> {
    Agent<T> createAgent();
}
