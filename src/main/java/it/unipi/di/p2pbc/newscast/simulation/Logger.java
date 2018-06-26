package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.util.Collection;

public interface Logger<T> {
    void logNetworkState(Collection<Correspondent<T>> network);
}
