package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class NewsLogger implements Logger<Double> {
    private File statsLog;
    private double expectedValue;
    private double accuracy;
    private int step = 0;

    public NewsLogger(String directory, double expectedValue) {
        this(directory, expectedValue, 0.01);
    }

    public NewsLogger(String directory, double expectedValue, double accuracy) {
        if (!new File(directory).isDirectory())
            throw new IllegalArgumentException("Not a directory");

        this.expectedValue = expectedValue;
        this.accuracy = accuracy;
        statsLog = new File(directory + "/news-statistics.csv");

        try (Writer log = Files.newBufferedWriter(statsLog.toPath(), CREATE)) {
            log.write(logData("Step",
                    "Avg",
                    "Std",
                    "Min",
                    "25%",
                    "50%",
                    "75%",
                    "Max",
                    "HasCorrectValue"));
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }
    }

    private String logData(Object... data) {
        return Arrays.stream(data).map(Object::toString)
                .collect(Collectors.joining(",", "", "\n"));
    }

    @Override
    public void logNetworkState(Collection<Correspondent<Double>> network) {
        System.out.println("LOG: NEWS LOGGER: Step " + step);
        System.out.print("LOG: -- Computing news statistics... ");
        DescriptiveStatistics stats = new DescriptiveStatistics();

        double hasCorrectValue = 0.;

        for (Correspondent<Double> n : network) {
            double value = n.getAgent().getNews();
            stats.addValue(value);

            if (Math.abs(value - expectedValue) <= Math.abs(expectedValue*accuracy))
                hasCorrectValue++;
        }

        hasCorrectValue /= network.size();
        System.out.print("Done\nLOG: -- Writing network statistics... ");

        try (Writer log = Files.newBufferedWriter(statsLog.toPath(), APPEND)) {
            log.write(logData(step,
                    stats.getMean(),
                    stats.getStandardDeviation(),
                    stats.getMin(),
                    stats.getPercentile(25),
                    stats.getPercentile(50),
                    stats.getPercentile(75),
                    stats.getMax(),
                    hasCorrectValue));
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }

        System.out.println("Done");
        step++;
    }
}
