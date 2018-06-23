package it.unipi.di.p2pbc.newscast.core;

import java.util.List;

public class ConstAgent implements Agent<Double> {
    private Double value;

    public ConstAgent(Double value) {
        this.value = value;
    }

    @Override
    public Double getNews() {
        return value;
    }

    @Override
    public void updateNews(List<Double> news) {}
}
