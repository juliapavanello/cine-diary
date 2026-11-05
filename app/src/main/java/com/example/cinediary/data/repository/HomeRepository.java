package com.example.cinediary.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.cinediary.data.model.HomeData;
import com.example.cinediary.data.remote.tmdb.TmdbCache;
import com.example.cinediary.data.remote.tmdb.TmdbMovie;
import com.example.cinediary.data.remote.tmdb.TmdbReview;
import com.example.cinediary.data.remote.tmdb.TmdbUpdateScheduler;

import java.util.Collections;
import java.util.List;

/** Centraliza o acesso ao cache e ao processo de atualização do TMDB. */
public class HomeRepository {

    private final Context appContext;
    private final WorkManager workManager;

    public HomeRepository(Context context) {
        this.appContext = context.getApplicationContext();
        this.workManager = WorkManager.getInstance(appContext);
    }

    public HomeData obterDadosDoCache() {
        List<TmdbMovie> tendencias = TmdbCache.obterLista(
                appContext, TmdbCache.TENDENCIAS
        );
        List<TmdbMovie> topFilmes = TmdbCache.obterLista(
                appContext, TmdbCache.TOP_FILMES
        );
        List<TmdbMovie> topSeries = TmdbCache.obterLista(
                appContext, TmdbCache.TOP_SERIES
        );
        List<TmdbReview> reviews = TmdbCache.obterReviews(appContext);

        return new HomeData(
                ouListaVazia(tendencias),
                ouListaVazia(topFilmes),
                ouListaVazia(topSeries),
                ouListaVazia(reviews)
        );
    }

    public void agendarAtualizacao() {
        TmdbUpdateScheduler.agendar(appContext);
    }

    public LiveData<List<WorkInfo>> observarAtualizacaoInicial() {
        return workManager.getWorkInfosForUniqueWorkLiveData(
                TmdbUpdateScheduler.NOME_TRABALHO_INICIAL
        );
    }

    private <T> List<T> ouListaVazia(List<T> lista) {
        return lista != null ? lista : Collections.emptyList();
    }
}
