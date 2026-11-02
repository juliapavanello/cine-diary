package com.example.cinediary.data.remote.tmdb;

import com.google.gson.annotations.SerializedName;

public class TmdbReview {

    private String author;
    private String content;

    @SerializedName("created_at")
    private String createdAt;

    private String movieTitle;
    private String posterPath;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}