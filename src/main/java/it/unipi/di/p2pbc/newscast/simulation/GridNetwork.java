package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

/**
 * Class representing a grid network.
 *
 * @param <T> the news data type
 */
public class GridNetwork<T> extends EmptyNetwork<T> {

    /**
     * Creates a 2-dimensional grid network. Every added node will have an agent
     * provided by the given {@link AgentFactory}.
     *
     * @param agentFactory the custom {@link AgentFactory}
     * @param rows the number of rows of the grid
     * @param cols the number of columns of the grid
     */
    public GridNetwork(int rows, int cols, AgentFactory<T> agentFactory) {
        super(agentFactory);
        Correspondent<T>[][] matrix = new Correspondent[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Correspondent<T> corr = addPeer();
                matrix[i][j] = corr;

                if (i > 0) {
                    Correspondent<T> peer = matrix[i - 1][j];
                    link(corr, peer);
                }

                if (j > 0) {
                    Correspondent<T> peer = matrix[i][j - 1];
                    link(corr, peer);
                }
            }
        }
    }
}
