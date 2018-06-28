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

public class NewsLogger<T> extends Logger<T> {
    protected File log;
    protected int currentStep = 0;

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

    @Override
    public void logNetworkState(Collection<Correspondent<T>> network) {
        log("LOG: Writing news... ");
        toCSV(network.stream().map(n -> n.getAgent().getNews()).toArray());
        log("Done\n");
    }
}
