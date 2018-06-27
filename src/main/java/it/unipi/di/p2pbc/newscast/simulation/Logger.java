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

public abstract class Logger<T> {
    protected int currentStep = 0;

    abstract void logNetworkState(Collection<Correspondent<T>> network);

    protected void toCSV(File file, Object... data) {
        try (Writer log = Files.newBufferedWriter(file.toPath(), APPEND)) {
            log.write(Arrays.stream(data).map(Object::toString)
                    .collect(Collectors.joining(",", currentStep++ + ",", "\n")));
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }
    }
}
