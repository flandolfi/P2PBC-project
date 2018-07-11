package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
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
    protected boolean multigraph;
    protected int id = 0;

    public NetworkLogger(String directory) {
        this(directory, true);
    }

    public NetworkLogger(String directory, boolean multigraph) {
        root = new File(directory);
        root.mkdirs();
        this.multigraph = multigraph;
    }

    public static void storeGraph(Graph graph, File directory) {
        log("LOG: Writing graph to GraphML file... ");
        FileSinkGraphML sink = new FileSinkGraphML();

        try (Writer writer = Files.newBufferedWriter(Paths.get(directory.getPath() + "/" + graph.getId() + ".xml"), CREATE, TRUNCATE_EXISTING)) {
            sink.writeAll(graph, writer);
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }

        log("Done\n");
    }

     public static <S> Graph loadGraph(Collection<Correspondent<S>> network, String id) {
        return loadGraph(network, id, true);
     }

    public static <S> Graph loadGraph(Collection<Correspondent<S>> network, String id, boolean multigraph) {
        Graph graph = multigraph? new MultiGraph(id, false, true)
                : new SingleGraph(id, false, true);
        Integer nodeId = 0, edgeId = 0;

        for (Correspondent<S> n : network) {
            log("\rLOG: Creating graph model... " + ++nodeId + "/" + network.size());
            graph.addNode(n.getId()).setAttribute("value", n.getAgent().getNews());

            for (Correspondent<S> e : n.getCache().getPeers()) {
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
