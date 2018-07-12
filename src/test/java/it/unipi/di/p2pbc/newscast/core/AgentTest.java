package it.unipi.di.p2pbc.newscast.core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest {
    private Random random = new Random();

    @Test
    void getNews() {
        Double value = random.nextDouble();
        Agent<Double> max = new MaxAgent(value);
        Agent<Double> avg = new AvgAgent(value);
        Agent<Double> cst = new ConstAgent(value);
        assertEquals(value, max.getNews());
        assertEquals(value, avg.getNews());
        assertEquals(value, cst.getNews());
    }

    @Test
    void updateNews() {
        ArrayList<Double> news = new ArrayList<>();
        Double value = random.nextDouble();
        int elements = 10;
        Double avg = value;
        Double max = value;
        Double cst = value;

        Agent<Double> maxAgent = new MaxAgent(value);
        Agent<Double> avgAgent = new AvgAgent(value);
        Agent<Double> cstAgent = new ConstAgent(value);

        for (int i = 0; i < elements; i++) {
            value = random.nextDouble();
            news.add(value);
        }

        max = Math.max(max, value);
        avg = 0.5 * (avg + value);
        maxAgent.updateNews(news);
        avgAgent.updateNews(news);
        cstAgent.updateNews(news);

        assertEquals(max, maxAgent.getNews());
        assertEquals(avg, avgAgent.getNews());
        assertEquals(cst, cstAgent.getNews());
    }
}