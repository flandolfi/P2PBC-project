package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class NewsStatsLogger extends NewsLogger<Double> {
    private double expectedValue;
    private double accuracy;

    public NewsStatsLogger(String filePath, double expectedValue) {
        this(filePath, expectedValue, 0.01);
    }

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
