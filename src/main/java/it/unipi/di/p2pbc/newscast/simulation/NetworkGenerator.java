package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.util.Set;

public interface NetworkGenerator<T> {
    Set<Correspondent<T>> getNetwork();
    int size();
    void resize(float factor);
}
