package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static org.graphstream.algorithm.Toolkit.averageClusteringCoefficient;
import static org.graphstream.algorithm.Toolkit.averageDegree;

/**
 * This {@link Logger} subclass provides functionalities to analyze and store measurements regarding
 * the network's graph representation.
 *
 * @param <T> the news data type
 */
public class NetworkStatsLogger<T> extends NewsLogger<T> {
    private boolean multigraph;

    /**
     * Creates a {@link NetworkStatsLogger} object. All the analysis will be performed on the
     * multigraph representations of the networks.
     *
     * @param filePath the path where the log file will be created (overwritten if already exists)
     */
    public NetworkStatsLogger(String filePath) {
        this(filePath, true);
    }

    /**
     * Creates a {@link NetworkStatsLogger} object.
     *
     * @param filePath   the path where the log file will be created (overwritten if already
     *                   exists)
     * @param multigraph if {@code true}, all the analysis will be performed on the multigraph
     *                   representations of the networks
     */
    public NetworkStatsLogger(String filePath, boolean multigraph) {
        super(filePath);
        this.multigraph = multigraph;

        try (Writer writer = Files.newBufferedWriter(log.toPath(), CREATE, TRUNCATE_EXISTING)) {
            writer.write("Step,ClustCoeff,AvgInDegree,AvgOutDegree,ConnComponents," +
                    "LargestConnComponent,AvgPathLength\n");
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }
    }

    /**
     * Logs various metrics evaluated on the graph representation of the network on a CSV file.
     *
     * @param network the network to be analyzed
     */
    @Override
    public void logNetworkState(Collection<Correspondent<T>> network) {
        Graph graph = NetworkLogger.loadGraph(network, "", multigraph);
        log("LOG: Computing average clustering coefficient... ");
        double clusteringCoefficient = averageClusteringCoefficient(graph);
        log("Done\n");
        log("LOG: Computing average node degrees... ");

        double avgOutDegree = 0.;

        for (Correspondent<T> n : network) {
            avgOutDegree += n.getPeers().size();
        }

        avgOutDegree /= network.size();

        double avgInDegree = averageDegree(graph) - avgOutDegree;
        log("Done\n");
        log("LOG: Computing connected components... ");
        ConnectedComponents cc = new ConnectedComponents();
        cc.init(graph);
        double connectedComponents = cc.getConnectedComponentsCount();
        int largestConnectedComponentSize = cc.getGiantComponent() == null ?
                0 : cc.getGiantComponent().size();
        log("Done\n");

        double avgPathLengths = 0.;
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.init(graph);

        for (int i = 0; i < graph.getNodeCount(); i++) {
            log("\rLOG: Computing shortest paths... " + (i + 1) + "/" + network.size());
            dijkstra.setSource(graph.getNode(i));
            dijkstra.compute();

            double totalPathLength = 0.;

            for (int j = i + 1; j < graph.getNodeCount(); j++) {
                totalPathLength += dijkstra.getPathLength(graph.getNode(j));
            }

            avgPathLengths += totalPathLength;
        }

        avgPathLengths /= graph.getNodeCount() * (graph.getNodeCount() - 1) * 0.5;
        log("\nLOG: Writing network statistics... ");
        toCSV(clusteringCoefficient,
                avgInDegree,
                avgOutDegree,
                connectedComponents,
                largestConnectedComponentSize,
                avgPathLengths);
        log("Done\n");
    }
}
