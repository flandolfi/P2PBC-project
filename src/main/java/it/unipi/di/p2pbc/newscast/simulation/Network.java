package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.util.List;

/**
 * Interface for a network class, with basic methods needed to allow interaction with the {@link
 * Coordinator} class.
 *
 * @param <T> the news data type
 */
public interface Network<T> {

    /**
     * Returns the {@link Correspondent}s in the network.
     *
     * @return a {@link List} of {@link Correspondent}s
     */
    List<Correspondent<T>> getNodes();

    /**
     * Returns the current size of the network.
     *
     * @return the current size of the network
     */
    int size();

    /**
     * Resizes the network to a given size. If the new size is smaller than the actual size,
     * randomly removes nodes from the network. Otherwise, link new nodes to the network performing
     * an update to a random node in the network.
     *
     * @param size the new size of the network
     */
    void resize(int size);
}
