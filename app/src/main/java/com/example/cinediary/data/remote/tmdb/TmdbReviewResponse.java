package com.example.cinediary.data.remote.tmdb;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TmdbReviewResponse {

    private List<TmdbReview> results;

    public List<TmdbReview> getResults() {
        return results;
    }
}