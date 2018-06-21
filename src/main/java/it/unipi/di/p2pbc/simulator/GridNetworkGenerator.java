package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Correspondent;

public class GridNetworkGenerator<T> extends EmptyNetworkGenerator<T> {
    public GridNetworkGenerator(int rows, int cols, AgentFactory<T> agentFactory) {
        super(agentFactory);
        Correspondent<T>[][] matrix = new Correspondent[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Correspondent<T> corr = addRandomPeer();
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
