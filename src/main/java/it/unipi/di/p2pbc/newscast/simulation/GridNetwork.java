package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

public class GridNetwork<T> extends EmptyNetwork<T> {
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
