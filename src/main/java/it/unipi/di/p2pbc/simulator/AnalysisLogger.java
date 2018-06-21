package main.java.it.unipi.di.p2pbc.simulator;

import main.java.it.unipi.di.p2pbc.newscast.Correspondent;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.graphstream.algorithm.Toolkit.averageClusteringCoefficient;
import static org.graphstream.algorithm.Toolkit.averageDegree;

public class AnalysisLogger<T> implements Logger<T> {
    private File root;
    private File pathLengthLog;
    private File clusteringCoefficientLog;
    private File nodeDegreeLog;
    private File connectivityLog;

    public AnalysisLogger(String directoryPath) {
        File parent = new File(directoryPath);

        if (!parent.isDirectory()) {
            throw new IllegalArgumentException("Not a directory");
        }

        root = new File(directoryPath + "/simulation-" + new Date().getTime());
        root.mkdirs();

        pathLengthLog = new File(root.getPath() + "/pathLength.csv");
        clusteringCoefficientLog = new File(root.getPath() + "/clusteringCoefficient.csv");
        nodeDegreeLog = new File(root.getPath() + "/nodeDegree.csv");
        connectivityLog = new File(root.getPath() + "/connectivity.csv");
    }

    private String logData(Object... data) {
        StringBuilder builder = new StringBuilder();

        for (Object datum : data) {
            builder.append(datum.toString()).append("\t");
        }

        return builder.append("\n").toString();
    }

    @Override
    public void logNetworkState(Collection<Correspondent<T>> network, int step) {
        Graph graph = new SingleGraph("step-" + step, false, true);
        Integer edgeId = 0;
        Integer nodeId = 0;

        for (Correspondent<T> n : network) {
            System.out.print("LOG: Creating graph model... " + nodeId++ + "/" + network.size());
            graph.addNode(n.getAddress().toString());

            for (Correspondent<T> e : n.getPeers()) {
                graph.addEdge((edgeId++).toString(), n.getAddress().toString(), e.getAddress().toString());
            }
        }

        System.out.print("LOG: Writing graph to DOT file... ");
        FileSinkDOT sink = new FileSinkDOT();

        try (Writer log = Files.newBufferedWriter(Paths.get(root.getPath() + "/network-" + step + ".dot"), CREATE)) {
            sink.writeAll(graph, log);
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }

        System.out.println("Done");
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
