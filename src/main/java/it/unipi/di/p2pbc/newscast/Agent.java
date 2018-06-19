package main.java.it.unipi.di.p2pbc.newscast;

import java.util.List;

public interface Agent<T> {
    T getNews();
    void updateNews(List<T> news);
}
