package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.util.List;

public interface Network<T> {
    List<Correspondent<T>> getNodes();
    int size();
    void resize(int size);
}
