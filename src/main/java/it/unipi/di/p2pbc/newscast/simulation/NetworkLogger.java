package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkGraphML;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE;

public class NetworkLogger<T> extends Logger<T> {
    protected SingleGraph graph = new SingleGraph("");
    protected File root;

    public NetworkLogger(String directory) {
        if (!new File(directory).isDirectory())
            throw new IllegalArgumentException("Not a directory");

        root = new File(directory);
    }

    protected void storeGraph() {
        System.out.print("LOG: -- Writing graph to GraphML file... ");
        FileSinkGraphML sink = new FileSinkGraphML();

        try (Writer log = Files.newBufferedWriter(Paths.get(root.getPath() + "/network-" + currentStep + ".xml"), CREATE)) {
            sink.writeAll(graph, log);
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }

        System.out.println("Done");
        currentStep++;
    }

    protected void loadGraph(Collection<Correspondent<T>> network) {
        graph = new SingleGraph("currentStep-" + currentStep, false, true);
        Integer nodeId = 0, edgeId = 0;

        for (Correspondent<T> n : network) {
            System.out.print("\rLOG: -- Creating graph model... " + ++nodeId + "/" + network.size());
            graph.addNode(n.getId()).setAttribute("value", n.getAgent().getNews());

            for (Correspondent<T> e : n.getPeers()) {
                graph.addEdge((edgeId++).toString(), n.getId(), e.getId());
            }
        }

        System.out.println();
    }

    @Override
    public void logNetworkState(Collection<Correspondent<T>> network) {
        System.out.println("LOG: NETWORK LOGGER: Step " + currentStep);
        loadGraph(network);
        storeGraph();
    }
}
