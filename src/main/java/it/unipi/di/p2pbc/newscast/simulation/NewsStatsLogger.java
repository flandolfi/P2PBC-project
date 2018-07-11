package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * This {@link Logger} subclass provides functionalities to analyze and store
 * statistics regarding the network's news values.
 */
public class NewsStatsLogger extends NewsLogger<Double> {
    private double expectedValue;
    private double accuracy;

    /**
     * Creates a {@link NewsStatsLogger} object. In addition to statistical
     * measures, it will evaluate the portion of
     * {@link it.unipi.di.p2pbc.newscast.core.Agent}s that store a given value
     * with an accuracy of 1%.
     *
     * @param filePath the path where the log file will be created (overwritten
     *                 if already exists)
     * @param expectedValue the expected value of the news to be stored by every
     *                      {@link it.unipi.di.p2pbc.newscast.core.Agent}
     */
    public NewsStatsLogger(String filePath, double expectedValue) {
        this(filePath, expectedValue, 0.01);
    }

    /**
     * Creates a {@link NewsStatsLogger} object. In addition to statistical
     * measures, it will evaluate the portion of
     * {@link it.unipi.di.p2pbc.newscast.core.Agent}s that store a given value
     * with a given accuracy.
     *
     * @param filePath the path where the log file will be created (overwritten
     *                 if already exists)
     * @param expectedValue the expected value of the news to be stored by every
     *                      {@link it.unipi.di.p2pbc.newscast.core.Agent}
     * @param accuracy the accuracy tolerance
     */
    public NewsStatsLogger(String filePath, double expectedValue, double accuracy) {
        super(filePath);

        this.expectedValue = expectedValue;
        this.accuracy = accuracy;

        try (Writer writer = Files.newBufferedWriter(log.toPath(), CREATE, TRUNCATE_EXISTING)) {
            writer.write("Step,Avg,Std,Min,25%,50%,75%,Max,HasCorrectValue\n");
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }
    }

    /**
     * Logs the news statistics regarding a given network.
     *
     * @param network the {@link Network} to be analyzed
     */
    @Override
    public void logNetworkState(Collection<Correspondent<Double>> network) {
        log("LOG: Computing news statistics... ");
        DescriptiveStatistics stats = new DescriptiveStatistics();

        double hasCorrectValue = 0.;

        for (Correspondent<Double> n : network) {
            double value = n.getAgent().getNews();
            stats.addValue(value);

            if (Math.abs(value - expectedValue) <= Math.abs(expectedValue*accuracy))
                hasCorrectValue++;
        }

        hasCorrectValue /= network.size();
        log("Done\nLOG: Writing network statistics... ");
        toCSV(stats.getMean(),
                stats.getStandardDeviation(),
                stats.getMin(),
                stats.getPercentile(25),
                stats.getPercentile(50),
                stats.getPercentile(75),
                stats.getMax(),
                hasCorrectValue);
        log("Done\n");
    }
}
