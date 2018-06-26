package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Node;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.graphstream.algorithm.Toolkit.averageClusteringCoefficient;
import static org.graphstream.algorithm.Toolkit.averageDegree;

public class AnalysisLogger extends NetworkLogger<Double> {
    private File statsLog;

    public AnalysisLogger(String directory) {
        super(directory);
        statsLog = new File(directory + "/network-statistics.csv");

        try (Writer log = Files.newBufferedWriter(statsLog.toPath(), CREATE)) {
            log.write(logData("Step",
                    "ClustCoeff",
                    "AvgInDegree",
                    "AvgOutDegree",
                    "ConnComponents",
                    "AvgPathLength"));
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }
    }

    private String logData(Object... data) {
        return Arrays.stream(data).map(Object::toString)
                .collect(Collectors.joining(",", "", "\n"));
    }

    @Override
    public void logNetworkState(Collection<Correspondent<Double>> network) {
        System.out.println("LOG: NETWORK ANALYSIS LOGGER: Step " + step);
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

        try (Writer log = Files.newBufferedWriter(statsLog.toPath(), APPEND)) {
            log.write(logData(step,
                    clusteringCoefficient,
                    avgInDegree,
                    avgOutDegree,
                    connectedComponents,
                    avgPathLengths));
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }

        System.out.println("Done");
        step++;
    }
}
