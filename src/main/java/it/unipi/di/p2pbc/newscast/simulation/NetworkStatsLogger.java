package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.*;
import java.nio.file.Files;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static org.graphstream.algorithm.Toolkit.averageClusteringCoefficient;
import static org.graphstream.algorithm.Toolkit.averageDegree;

public class NetworkStatsLogger extends NewsLogger<Double> {
    public NetworkStatsLogger(String directory) {
        super(directory);

        try (Writer writer = Files.newBufferedWriter(log.toPath(), CREATE, TRUNCATE_EXISTING)) {
            writer.write("Step,ClustCoeff,AvgInDegree,AvgOutDegree,ConnComponents,AvgPathLength\n");
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }
    }

    @Override
    public void logNetworkState(Collection<Correspondent<Double>> network) {
        SingleGraph graph = NetworkLogger.loadGraph(network, "");
        log("LOG: Computing average clustering coefficient... ");
        double clusteringCoefficient = averageClusteringCoefficient(graph);
        log("Done\n");
        log("LOG: Computing average node degrees... ");

        double avgOutDegree = 0.;

        for (Correspondent<Double> n : network) {
            avgOutDegree += n.getPeers().size();
        }

        avgOutDegree /= network.size();

        double avgInDegree = averageDegree(graph) - avgOutDegree;
        log("Done\n");
        log("LOG: Computing connected components... ");
        ConnectedComponents cc = new ConnectedComponents();
        cc.init(graph);
        double connectedComponents = cc.getConnectedComponentsCount();
        log("Done\n");

        int nodeId = 0;
        double avgPathLengths = 0.;
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.init(graph);

        for (Node n : graph.getNodeSet()) {
            log("\rLOG: Computing shortest paths... " + ++nodeId + "/" + network.size());
            dijkstra.setSource(n);
            dijkstra.compute();

            double totalPathLength = 0.;

            for (Node target : graph.getNodeSet()) {
                totalPathLength += dijkstra.getPathLength(target);
            }

            avgPathLengths += totalPathLength/graph.getNodeCount();
        }

        avgPathLengths /= graph.getNodeCount();
        log("\nLOG: Writing network statistics... ");
        toCSV(clusteringCoefficient,
                avgInDegree,
                avgOutDegree,
                connectedComponents,
                avgPathLengths);
        log("Done\n");
    }
}
