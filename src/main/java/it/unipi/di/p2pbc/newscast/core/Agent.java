package it.unipi.di.p2pbc.newscast.core;

import java.util.List;

public interface Agent<T> {
    T getNews();
    void updateNews(List<T> news);
}
