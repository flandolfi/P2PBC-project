package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Correspondent;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.*;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Date;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.graphstream.algorithm.Toolkit.averageClusteringCoefficient;
import static org.graphstream.algorithm.Toolkit.averageDegree;

public class AnalysisLogger implements Logger<Double> {
    private File pathLengthLog;
    private File clusteringCoefficientLog;
    private File nodeDegreeLog;
    private File connectivityLog;

    public AnalysisLogger(String directoryPath) {
        File parent = new File(directoryPath);

        if (!parent.isDirectory()) {
            throw new IllegalArgumentException("Not a directory");
        }

        File directory = new File(directoryPath + "/simulation-" + new Date().getTime());
        directory.mkdirs();

        pathLengthLog = new File(directory.getPath() + "/pathLength.csv");
        clusteringCoefficientLog = new File(directory.getPath() + "/clusteringCoefficient.csv");
        nodeDegreeLog = new File(directory.getPath() + "/nodeDegree.csv");
        connectivityLog = new File(directory.getPath() + "/connectivity.csv");
    }

    private String logData(Object... data) {
        StringBuilder builder = new StringBuilder();

        for (Object datum : data) {
            builder.append(datum.toString()).append("\t");
        }

        return builder.append("\n").toString();
    }

    @Override
    public void logNetworkState(Collection<Correspondent<Double>> network, int step) {
        Graph graph = new SingleGraph("step-" + step, false, true);
        Integer edgeId = 0;
        Integer nodeId = 0;

        for (Correspondent<Double> n : network) {
            System.out.print("LOG: Creating graph model... " + nodeId++ + "/" + network.size());
            graph.addNode(n.getAddress().toString());

            for (Correspondent<Double> e : n.getPeers()) {
                graph.addEdge((edgeId++).toString(), n.getAddress().toString(), e.getAddress().toString());
            }
        }

        System.out.print("\nLOG: Computing average clustering coefficient... ");
        double clusteringCoefficient = averageClusteringCoefficient(graph);
        System.out.println("Done");
        System.out.print("LOG: Writing average clustering coefficient... ");

        try (Writer log = Files.newBufferedWriter(clusteringCoefficientLog.toPath(), CREATE, APPEND)) {
            log.write(logData(step, clusteringCoefficient));
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }

        System.out.println("Done");
        System.out.print("LOG: Computing average node degrees... ");
        double nodeDegrees = averageDegree(graph);
        System.out.println("Done");
        System.out.print("LOG: Writing average node degrees... ");

        try (Writer log = Files.newBufferedWriter(nodeDegreeLog.toPath(), CREATE, APPEND)) {
            log.write(logData(step, nodeDegrees));
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }

        System.out.println("Done");

        System.out.print("LOG: Computing connected components... ");
        ConnectedComponents cc = new ConnectedComponents();
        cc.init(graph);
        double connectedComponents = cc.getConnectedComponentsCount();
        System.out.println("Done");
        System.out.print("LOG: Writing connected components... ");

        try (Writer log = Files.newBufferedWriter(connectivityLog.toPath(), CREATE, APPEND)) {
            log.write(logData(step, connectedComponents));
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }

        System.out.println("Done");

        if (connectedComponents != 1)
            return;

        nodeId = 0;
        double avgPathLengths = 0.;
        System.out.println();
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.init(graph);

        for (Node n : graph.getNodeSet()) {
            System.out.print("LOG: Computing shortest paths... " + nodeId++ + "/" + network.size());
            dijkstra.setSource(n);
            dijkstra.compute();

            double totalPathLength = 0.;

            for (Node target : graph.getNodeSet()) {
                totalPathLength += dijkstra.getPathLength(target);
            }

            avgPathLengths += totalPathLength/graph.getNodeCount();
        }

        System.out.print("\nLOG: Writing shortest paths... ");
        avgPathLengths /= graph.getNodeCount();

        try (Writer log = Files.newBufferedWriter(pathLengthLog.toPath(), CREATE, APPEND)) {
            log.write(logData(step, avgPathLengths));
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }

        System.out.println("Done");
    }
}
