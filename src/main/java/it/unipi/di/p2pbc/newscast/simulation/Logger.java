package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.io.PrintStream;
import java.util.Collection;

public abstract class Logger<T> {
    private static PrintStream out = System.err;
    private static boolean verbose = true;

    public abstract void logNetworkState(Collection<Correspondent<T>> network);

    public static void setVerbose(boolean verbose) {
        Logger.verbose = verbose;
    }

    public static void setPrintStream(PrintStream out) {
        Logger.out = out;
    }

    public static void log(String string) {
        if (verbose)
            out.print(string);
    }
}
