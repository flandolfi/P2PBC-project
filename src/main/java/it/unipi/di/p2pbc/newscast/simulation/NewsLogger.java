package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * This {@link Logger} subclass stores each {@link Correspondent}'s
 * {@link it.unipi.di.p2pbc.newscast.core.Agent}'s news to a CSV file.
 *
 * @param <T> the news data type
 */
public class NewsLogger<T> extends Logger<T> {
    protected File log;
    protected int currentStep = 0;

    /**
     * Creates a {@link NewsLogger} object.
     *
     * @param filePath the file where the news will be logged (it will be
     *                 created or overwritten if already existent)
     */
    public NewsLogger(String filePath) {
        log = new File(filePath);
        log.getParentFile().mkdirs();

        try (Writer writer = Files.newBufferedWriter(log.toPath(), CREATE, TRUNCATE_EXISTING)) {
            writer.write("");
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }
    }

    protected void toCSV(Object... data) {
        try (Writer writer = Files.newBufferedWriter(log.toPath(), APPEND)) {
            writer.write(Arrays.stream(data).map(Object::toString)
                    .collect(Collectors.joining(",", currentStep++ + ",", "\n")));
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }
    }

    /**
     * Sets the current cycle to be logged as index. Every successive call will
     * have this value incremented by one.
     *
     * @param step the current step to be displayed
     */
    public void setCurrentStep(int step) {
        this.currentStep = step;
    }

    /**
     * Returns the current step (index).
     *
     * @return the current step
     */
    public int getCurrentStep() {
        return currentStep;
    }

    /**
     * Appends the current news in the network to a CSV file.
     *
     * @param network the {@link Network} to be analyzed
     */
    @Override
    public void logNetworkState(Collection<Correspondent<T>> network) {
        log("LOG: Writing news... ");
        toCSV(network.stream().map(n -> n.getAgent().getNews()).toArray());
        log("Done\n");
    }
}
