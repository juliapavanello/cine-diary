
package com.example.cinediary.data.remote.tmdb;

import com.google.gson.annotations.SerializedName;

public class TmdbMovie {

    private int id;

    private String title;

    private String name;

    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("release_date")
    private String releaseDate;

    // Identificador do filme
    public int getId() {
        return id;
    }

    // Nome do filme ou série
    public String getTitle() {
        return title != null ? title : name;
    }

    // Sinopse
    public String getOverview() {
        return overview;
    }

    // Caminho da imagem do pôster
    public String getPosterPath() {
        return posterPath;
    }

    // Caminho da imagem de fundo
    public String getBackdropPath() {
        return backdropPath;
    }

    // Avaliação da TMDB
    public double getVoteAverage() {
        return voteAverage;
    }

    // Data de lançamento
    public String getReleaseDate() {
        return releaseDate;
    }
}
