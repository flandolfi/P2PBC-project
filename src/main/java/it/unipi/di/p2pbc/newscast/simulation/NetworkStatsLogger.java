package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Node;

import java.io.*;
import java.nio.file.Files;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE;
import static org.graphstream.algorithm.Toolkit.averageClusteringCoefficient;
import static org.graphstream.algorithm.Toolkit.averageDegree;

public class NetworkStatsLogger extends NetworkLogger<Double> {
    private File statsLog;

    public NetworkStatsLogger(String directory) {
        super(directory);
        statsLog = new File(directory + "/network-statistics.csv");

        try (Writer log = Files.newBufferedWriter(statsLog.toPath(), CREATE)) {
            log.write("Step,ClustCoeff,AvgInDegree,AvgOutDegree,ConnComponents,AvgPathLength\n");
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }
    }

    @Override
    public void logNetworkState(Collection<Correspondent<Double>> network) {
        System.out.println("LOG: NETWORK ANALYSIS LOGGER: Step " + currentStep);
        loadGraph(network);
        System.out.print("LOG: -- Computing average clustering coefficient... ");
        double clusteringCoefficient = averageClusteringCoefficient(graph);
        System.out.println("Done");
        System.out.print("LOG: -- Computing average node degrees... ");

        double avgOutDegree = 0.;

        for (Correspondent<Double> n : network) {
            avgOutDegree += n.getPeers().size();
        }

        avgOutDegree /= network.size();

        double avgInDegree = averageDegree(graph) - avgOutDegree;
        System.out.println("Done");
        System.out.print("LOG: -- Computing connected components... ");
        ConnectedComponents cc = new ConnectedComponents();
        cc.init(graph);
        double connectedComponents = cc.getConnectedComponentsCount();
        System.out.println("Done");

        int nodeId = 0;
        double avgPathLengths = 0.;
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.init(graph);

        for (Node n : graph.getNodeSet()) {
            System.out.print("\rLOG: -- Computing shortest paths... " + ++nodeId + "/" + network.size());
            dijkstra.setSource(n);
            dijkstra.compute();

            double totalPathLength = 0.;

            for (Node target : graph.getNodeSet()) {
                totalPathLength += dijkstra.getPathLength(target);
            }

            avgPathLengths += totalPathLength/graph.getNodeCount();
        }

        avgPathLengths /= graph.getNodeCount();
        System.out.print("\nLOG: -- Writing network statistics... ");
        toCSV(statsLog,
                clusteringCoefficient,
                avgInDegree,
                avgOutDegree,
                connectedComponents,
                avgPathLengths);
        System.out.println("Done");
    }
}
