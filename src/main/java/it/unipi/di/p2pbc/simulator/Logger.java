package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Correspondent;

import java.util.Collection;

public interface Logger<T> {
    void logNetworkState(Collection<Correspondent<T>> network, int step);
}
