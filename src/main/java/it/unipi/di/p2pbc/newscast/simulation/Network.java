package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.util.Collection;

public interface Network<T> {
    Collection<Correspondent<T>> getNodes();
    int size();
    void resize(double factor);
}
