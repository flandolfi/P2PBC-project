package it.unipi.di.p2pbc.newscast.core;

import java.util.List;

/**
 * This class represent an {@link Agent} application that ignores all updates
 * and maintains the stored news constant.
 */
public class ConstAgent implements Agent<Double> {
    private Double value;

    /**
     * Constructs and initialize an {@link ConstAgent} instance.
     *
     * @param value the (constant) value of the news
     */
    public ConstAgent(Double value) {
        this.value = value;
    }

    /**
     * Returns the news stored by the agent.
     *
     * @return the news value
     */
    @Override
    public Double getNews() {
        return value;
    }

    /**
     * Dummy function to respect the interface.
     *
     * @param news a {@link List} of {@link Double}
     */
    @Override
    public void updateNews(List<Double> news) {}
}
