package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;
import org.graphstream.graph.Graph;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static org.graphstream.algorithm.Toolkit.degreeDistribution;

/**
 * {@link Logger} subclass used to log the degree distribution of {@link Network}s.
 */
public class DegreeDistributionLogger extends NewsLogger<Double> {
    private boolean multigraph;

    /**
     * Creates a {@link Logger} subclass which will log down the (multigraph) degree distribution at
     * each step of the protocol.
     *
     * @param filePath the path on which the log file will be create (overwritten if already
     *                 present)
     */
    public DegreeDistributionLogger(String filePath) {
        this(filePath, true);
    }

    /**
     * Creates a {@link Logger} subclass which will log down the degree distribution at each step of
     * the protocol.
     *
     * @param filePath   the path on which the log file will be create (overwritten if already
     *                   present)
     * @param multigraph if {@code true}, treats the {@link Network} as a multigraph (i.e., allow
     *                   multiple edges between two nodes).
     */
    public DegreeDistributionLogger(String filePath, boolean multigraph) {
        super(filePath);
        this.multigraph = multigraph;

        try (Writer writer = Files.newBufferedWriter(log.toPath(), CREATE, TRUNCATE_EXISTING)) {
            writer.write("");
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }
    }

    /**
     * Logs the the degree distribution of the given {@link Network}.
     *
     * @param network the {@link Network} to analyze
     */
    @Override
    public void logNetworkState(Collection<Correspondent<Double>> network) {
        Graph graph = NetworkLogger.loadGraph(network, "", multigraph);
        log("LOG: Computing degree distribution... ");
        int[] degrees = degreeDistribution(graph);
        log("Done\n");
        log("LOG: Writing degree distribution... ");
        toCSV(Arrays.stream(degrees).boxed().toArray());
        log("Done\n");
    }
}
