package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class NewsLogger<T> extends Logger<T> {
    protected File newsLog;

    public NewsLogger(String directory) {
        if (!new File(directory).isDirectory())
            throw new IllegalArgumentException("Not a directory");

        newsLog = new File(directory + "/news.csv");

        try {
            newsLog.createNewFile();
        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
        }
    }

    @Override
    public void logNetworkState(Collection<Correspondent<T>> network) {
        System.out.println("LOG: NEWS LOGGER: Step " + currentStep);
        System.out.print("LOG: -- Writing news... ");
        toCSV(newsLog, network.stream().map(n -> n.getAgent().getNews()).toArray());
        System.out.println("Done");
    }
}
