package it.unipi.di.p2pbc.newscast.core;

import java.util.List;

/**
 * This class represents the agent application that interchanges information
 * with a {@link Correspondent} object.
 *
 * @param <T> the value type of the news stored by the agent.
 */
public interface Agent<T> {

    /**
     * Returns the current news stored by the agent.
     *
     * @return the current news
     */
    T getNews();

    /**
     * Updates the news stored by the agent.
     *
     * @param news a {@link List} of news of the same type of the one stored by
     *             the agent
     */
    void updateNews(List<T> news);
}
