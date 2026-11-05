package com.example.cinediary.ui.profile;

/** Modelo simples dos filmes favoritos exibidos no perfil. */
public class FavoriteMovie {

    private final int position;
    private final String title;
    private final String genres;
    private final String metadata;
    private final String rating;
    private final int posterResource;

    public FavoriteMovie(int position, String title, String genres, String metadata,
                         String rating, int posterResource) {
        this.position = position;
        this.title = title;
        this.genres = genres;
        this.metadata = metadata;
        this.rating = rating;
        this.posterResource = posterResource;
    }

    public int getPosition() { return position; }
    public String getTitle() { return title; }
    public String getGenres() { return genres; }
    public String getMetadata() { return metadata; }
    public String getRating() { return rating; }
    public int getPosterResource() { return posterResource; }
}
