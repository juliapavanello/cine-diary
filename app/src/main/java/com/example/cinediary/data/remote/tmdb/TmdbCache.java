package com.example.cinediary.data.remote.tmdb;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TmdbCache {

    private static final String PREFS = "tmdb_cache";

    public static final String TENDENCIAS = "trending_movies";
    public static final String TOP_FILMES = "top_movies";
    public static final String TOP_SERIES = "top_series";
    public static final String REVIEWS = "movie_reviews";

    public static boolean salvarLista(Context context, String chave, List<TmdbMovie> filmes) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        String novoJson = new Gson().toJson(filmes);
        String antigoJson = prefs.getString(chave, null);

        if (novoJson.equals(antigoJson)) return false;

        prefs.edit().putString(chave, novoJson).apply();
        return true;
    }

    public static List<TmdbMovie> obterLista(Context context, String chave) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = prefs.getString(chave, null);

        if (json == null) return null;

        Type tipo = new TypeToken<List<TmdbMovie>>() {}.getType();
        return new Gson().fromJson(json, tipo);
    }

    public static boolean salvarReviews(Context context, List<TmdbReview> reviews) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        String novoJson = new Gson().toJson(reviews);
        String antigoJson = prefs.getString(REVIEWS, null);

        if (novoJson.equals(antigoJson)) return false;

        prefs.edit().putString(REVIEWS, novoJson).apply();
        return true;
    }

    public static List<TmdbReview> obterReviews(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = prefs.getString(REVIEWS, null);

        if (json == null) return null;

        Type tipo = new TypeToken<List<TmdbReview>>() {}.getType();
        return new Gson().fromJson(json, tipo);
    }

    public static boolean temTodasListas(Context context) {
        List<TmdbMovie> tendencias = obterLista(context, TENDENCIAS);
        List<TmdbMovie> filmes = obterLista(context, TOP_FILMES);
        List<TmdbMovie> series = obterLista(context, TOP_SERIES);
        List<TmdbReview> reviews = obterReviews(context);

        return tendencias != null && !tendencias.isEmpty()
                && filmes != null && !filmes.isEmpty()
                && series != null && !series.isEmpty()
                && reviews != null;
    }
}