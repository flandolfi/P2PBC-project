package it.unipi.di.p2pbc.newscast.simulation;

import it.unipi.di.p2pbc.newscast.core.Correspondent;

import java.io.PrintStream;
import java.util.Collection;

/**
 * This abstract class provides the skeleton for a logger, that can be used both for {@link
 * Coordinator} objects and as standalone.
 *
 * @param <T> the type of news data
 */
public abstract class Logger<T> {
    private static PrintStream out = System.err;
    private static boolean verbose = true;

    /**
     * Logs the current state of the {@link Network}.
     *
     * @param network the {@link Network} to be analyzed
     */
    public abstract void logNetworkState(Collection<Correspondent<T>> network);

    /**
     * Sets the verbosity of the logger. If {@code verbose} is {@code false}, mutes all the log
     * output.
     *
     * @param verbose the verbosity of the logger
     */
    public static void setVerbose(boolean verbose) {
        Logger.verbose = verbose;
    }

    /**
     * Sets the {@link PrintStream} where the output of the logger should be printed. The default
     * value is {@link System#err}.
     *
     * @param out the {@link PrintStream} where the output of the logger should be printed
     */
    public static void setPrintStream(PrintStream out) {
        Logger.out = out;
    }

    /**
     * Prints a string on the logger's {@link PrintStream}.
     *
     * @param string the {@link String} to be printed
     * @see Logger#setPrintStream(PrintStream)
     */
    public static void log(String string) {
        if (verbose)
            out.print(string);
    }
}
