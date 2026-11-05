
package com.example.cinediary.data.remote.tmdb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface TmdbApi {

    // Busca filmes em tendência na semana
    @GET("trending/movie/week")
    Call<TmdbResponse> getTrendingMovies(
            @Header("Authorization") String authorization,
            @Query("language") String language
    );

    @GET("movie/top_rated")
    Call<TmdbResponse> getTopRatedMovies(
            @Header("Authorization") String authorization,
            @Query("language") String language
    );

    @GET("tv/top_rated")
    Call<TmdbResponse> getTopRatedSeries(
            @Header("Authorization") String authorization,
            @Query("language") String language
    );

    @GET("movie/{movieId}/reviews")
    Call<TmdbReviewResponse> getMovieReviews(
            @Header("Authorization") String authorization,
            @Path("movieId") int movieId,
            @Query("language") String language
    );

    // Detalhes de um filme + elenco (append_to_response=credits)
    @GET("movie/{id}")
    Call<TmdbDetalhes> getMovieDetails(
            @Header("Authorization") String authorization,
            @Path("id") int id,
            @Query("language") String language,
            @Query("append_to_response") String append
    );

    // Detalhes de uma série + elenco
    @GET("tv/{id}")
    Call<TmdbDetalhes> getTvDetails(
            @Header("Authorization") String authorization,
            @Path("id") int id,
            @Query("language") String language,
            @Query("append_to_response") String append
    );

    // Reviews de filme/série (usado como plano B em inglês na tela do filme)
    @GET("{tipo}/{id}/reviews")
    Call<TmdbDetalhes.Reviews> getReviews(
            @Header("Authorization") String authorization,
            @Path("tipo") String tipo,
            @Path("id") int id,
            @Query("language") String language
    );
}
