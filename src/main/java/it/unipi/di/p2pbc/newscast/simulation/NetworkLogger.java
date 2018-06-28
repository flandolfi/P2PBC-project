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
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class NetworkLogger<T> extends Logger<T> {
    protected File root;
    protected int id = 0;

    public NetworkLogger(String directory) {
        root = new File(directory);
        root.mkdirs();
    }

    public static void storeGraph(SingleGraph graph, File directory) {
        log("LOG: Writing graph to GraphML file... ");
        FileSinkGraphML sink = new FileSinkGraphML();

        try (Writer writer = Files.newBufferedWriter(Paths.get(directory.getPath() + "/network-" + graph.getId() + ".xml"), CREATE, TRUNCATE_EXISTING)) {
            sink.writeAll(graph, writer);
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }

        log("Done\n");
    }

    public static <S> SingleGraph loadGraph(Collection<Correspondent<S>> network, String id) {
        SingleGraph graph = new SingleGraph(id, false, true);
        Integer nodeId = 0, edgeId = 0;

        for (Correspondent<S> n : network) {
            log("\rLOG: Creating graph model... " + ++nodeId + "/" + network.size());
            graph.addNode(n.getId()).setAttribute("value", n.getAgent().getNews());

            for (Correspondent<S> e : n.getPeers()) {
                graph.addEdge((edgeId++).toString(), n.getId(), e.getId());
            }
        }

        log("\n");

        return graph;
    }

    @Override
    public void logNetworkState(Collection<Correspondent<T>> network) {
        storeGraph(loadGraph(network, "network-" + id++), root);
    }
}
