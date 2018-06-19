package main.java.it.unipi.di.p2pbc.simulator;

import java.util.Collection;

public interface Logger<Node> {
    void beginSession(int size);
    void endSession();
    void logNetworkState(Collection<Node> network, int step);
}
