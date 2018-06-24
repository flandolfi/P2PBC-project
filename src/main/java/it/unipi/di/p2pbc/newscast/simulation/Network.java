package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.util.Set;

public interface Network<T> {
    Set<Correspondent<T>> getNodes();
    int size();
    void resize(float factor);
}
