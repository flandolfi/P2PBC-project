package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Correspondent;

import java.util.Set;

public interface NetworkGenerator<T> {
    Set<Correspondent<T>> getNetwork();
    int size();
    void resize(float factor);
}
