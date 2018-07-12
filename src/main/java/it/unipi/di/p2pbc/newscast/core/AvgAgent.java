package it.unipi.di.p2pbc.newscast.core;

import java.util.List;

/**
 * This class represent an {@link Agent} application that updates the news adopting a
 * diffusion-based aggregation.
 */
public class AvgAgent implements Agent<Double> {
    private Double value;

    /**
     * Constructs and initializes an {@link AvgAgent} instance.
     *
     * @param value the initial value of the news
     */
    public AvgAgent(Double value) {
        this.value = value;
    }

    /**
     * Returns the current news stored by the agent.
     *
     * @return the current news
     */
    @Override
    public Double getNews() {
        return value;
    }

    /**
     * Updates the news stored by the agent. Only the last element of the list will be relevant to
     * the update, since it is assumed that the passed list is sorted by date of creation (and the
     * last one should be the one of the peer that is exchanging the news stored in the cache).
     *
     * @param news a {@link List} of {@link Double}
     */
    @Override
    public void updateNews(List<Double> news) {
        value = 0.5 * (value + news.get(news.size() - 1));
    }
}