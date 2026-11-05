package com.example.cinediary.data.model;

import com.example.cinediary.data.remote.tmdb.TmdbMovie;
import com.example.cinediary.data.remote.tmdb.TmdbReview;

import java.util.List;

/** Dados necessários para preencher a tela inicial. */
public class HomeData {

    private final List<TmdbMovie> tendencias;
    private final List<TmdbMovie> topFilmes;
    private final List<TmdbMovie> topSeries;
    private final List<TmdbReview> reviews;

    public HomeData(List<TmdbMovie> tendencias,
                    List<TmdbMovie> topFilmes,
                    List<TmdbMovie> topSeries,
                    List<TmdbReview> reviews) {
        this.tendencias = tendencias;
        this.topFilmes = topFilmes;
        this.topSeries = topSeries;
        this.reviews = reviews;
    }

    public List<TmdbMovie> getTendencias() {
        return tendencias;
    }

    public List<TmdbMovie> getTopFilmes() {
        return topFilmes;
    }

    public List<TmdbMovie> getTopSeries() {
        return topSeries;
    }

    public List<TmdbReview> getReviews() {
        return reviews;
    }
}
