package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Correspondent;

import java.util.List;

public interface NetworkGenerator<T> {
    List<Correspondent<T>> instantiateNetwork(int size);
}
