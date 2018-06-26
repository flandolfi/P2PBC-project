package it.unipi.di.p2pbc.newscast.core;

import java.util.List;

public class AvgAgent implements Agent<Double> {
    private Double value;

    public AvgAgent(Double value) {
        this.value = value;
    }

    @Override
    public Double getNews() {
        return value;
    }

    @Override
    public void updateNews(List<Double> news) {
        value =  0.5*(value + news.get(news.size() - 1));
    }
}