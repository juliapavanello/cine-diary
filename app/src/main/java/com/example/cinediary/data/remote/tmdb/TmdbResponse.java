
package com.example.cinediary.data.remote.tmdb;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TmdbResponse {

    private int page;

    private List<TmdbMovie> results;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    public int getPage() {
        return page;
    }

    public List<TmdbMovie> getResults() {
        return results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
