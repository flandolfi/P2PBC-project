package main.java.it.unipi.di.p2pbc.newscast;

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
        news.forEach(v -> value += v);
        value /= news.size() + 1.;
    }
}