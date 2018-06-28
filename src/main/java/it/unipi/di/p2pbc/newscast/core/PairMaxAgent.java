package it.unipi.di.p2pbc.newscast.core;

import java.util.List;

public class PairMaxAgent implements Agent<Double> {
    private Double value;

    public PairMaxAgent(Double value) {
        this.value = value;
    }

    @Override
    public Double getNews() {
        return value;
    }

    @Override
    public void updateNews(List<Double> news) {
        value = Math.max(value, news.get(news.size() - 1));
    }
}