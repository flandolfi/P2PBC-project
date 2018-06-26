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

public class NetworkLogger<T> implements Logger<T> {
    protected SingleGraph graph = new SingleGraph("");
    protected int step = 0;
    protected File root;

    public NetworkLogger(String directory) {
        root = new File(directory);
        root.mkdirs();
    }

    protected void storeGraph() {
        System.out.print("LOG: -- Writing graph to GraphML file... ");
        FileSinkGraphML sink = new FileSinkGraphML();

        try (Writer log = Files.newBufferedWriter(Paths.get(root.getPath() + "/network-" + step + ".xml"), CREATE)) {
            sink.writeAll(graph, log);
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }

        System.out.println("Done");
    }

    protected void loadGraph(Collection<Correspondent<T>> network) {
        graph = new SingleGraph("step-" + step, false, true);
        Integer nodeId = 0, edgeId = 0;

        for (Correspondent<T> n : network) {
            System.out.print("\rLOG: -- Creating graph model... " + ++nodeId + "/" + network.size());
            graph.addNode(n.getAddress().toString()).setAttribute("value", n.getAgent().getNews());

            for (Correspondent<T> e : n.getPeers()) {
                graph.addEdge((edgeId++).toString(), n.getAddress().toString(), e.getAddress().toString());
            }
        }

        System.out.println();
    }

    @Override
    public void logNetworkState(Collection<Correspondent<T>> network) {
        System.out.println("LOG: NETWORK LOGGER: Step " + step);
        loadGraph(network);
        storeGraph();
        step++;
    }
}
