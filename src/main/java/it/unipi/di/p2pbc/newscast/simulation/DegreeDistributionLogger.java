package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static org.graphstream.algorithm.Toolkit.degreeDistribution;

public class DegreeDistributionLogger extends NewsLogger<Double> {
    public DegreeDistributionLogger(String filePath) {
        super(filePath);

        try (Writer writer = Files.newBufferedWriter(log.toPath(), CREATE, TRUNCATE_EXISTING)) {
            writer.write("");
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }
    }

    @Override
    public void logNetworkState(Collection<Correspondent<Double>> network) {
        SingleGraph graph = NetworkLogger.loadGraph(network, "");
        log("LOG: Computing degree distribution... ");
        int[] degrees = degreeDistribution(graph);
        log("Done\n");
        log("LOG: Writing degree distribution... ");
        toCSV(Arrays.stream(degrees).boxed().toArray());
        log("Done\n");
    }
}