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

/**
 * This {@link Logger} subclass provides functionalities to store the graph
 * representation of the network as a GraphML file.
 *
 * @param <T> news data type
 */
public class NetworkLogger<T> extends Logger<T> {
    protected File root;
    protected boolean multigraph;
    protected int id = 0;

    /**
     * Creates a {@link NetworkLogger} that will store multigraph
     * representations of the network.
     *
     * @param directory the path of a directory where the GraphML files will be
     *                  stored
     */
    public NetworkLogger(String directory) {
        this(directory, true);
    }


    /**
     * Creates a {@link NetworkLogger} that will store graph representations of
     * the network.
     *
     * @param multigraph if {@code true}, the graph will be stored as a
     *                   multigraph (i.e., it will allow multiple edges between
     *                   nodes)
     * @param directory the path of a directory where the GraphML files will be
     *                  stored
     */
    public NetworkLogger(String directory, boolean multigraph) {
        root = new File(directory);
        root.mkdirs();
        this.multigraph = multigraph;
    }

    /**
     * Stores a given {@link Graph} in a directory.
     *
     * @param graph the {@link Graph} to be stored
     * @param directory the path of the directory
     */
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

    /**
     * Loads and converts a {@link Network} into a {@link MultiGraph}.
     *
     * @param network the {@link Network} to be converted
     * @param id the identifier of the {@link Graph}
     * @param <S> the type of the news data
     * @return returns a {@link Graph} object (safely castable to
     *         {@link MultiGraph})
     */
    public static <S> Graph loadGraph(Collection<Correspondent<S>> network, String id) {
        return loadGraph(network, id, true);
    }

    /**
     * Loads and converts a {@link Network} into a {@link SingleGraph} or a
     * {@link MultiGraph}, dependently on the value of {@code multigraph}.
     *
     * @param network the {@link Network} to be converted
     * @param id the identifier of the {@link Graph}
     * @param multigraph if {@code true}, loads the {@link Network} as a
     *                   {@link MultiGraph}, otherwise as a {@link SingleGraph}
     * @param <S> the type of the news data
     * @return returns a {@link Graph} object (safely castable to
     *         {@link MultiGraph})
     */
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

    /**
     * Stores the {@link Network} as a GraphML file.
     *
     * @param network the {@link Network} to be analyzed
     */
    @Override
    public void logNetworkState(Collection<Correspondent<T>> network) {
        storeGraph(loadGraph(network, "network-" + id++, multigraph), root);
    }
}
