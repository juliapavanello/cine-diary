package com.example.cinediary.data.remote.tmdb;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Response;

public class TmdbUpdateWorker extends Worker {

    private static final int MAX_REVIEWS = 5;
    private static final int MAX_MOVIES_TO_SEARCH = 10;

    public TmdbUpdateWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params
    ) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            TmdbApi api = TmdbClient.getApi();
            String token = TmdbClient.getAuthorizationHeader();

            Response<TmdbResponse> tendencias =
                    api.getTrendingMovies(token, "pt-BR").execute();

            Response<TmdbResponse> topFilmes =
                    api.getTopRatedMovies(token, "pt-BR").execute();

            Response<TmdbResponse> topSeries =
                    api.getTopRatedSeries(token, "pt-BR").execute();

            if (!validaResposta(tendencias)
                    || !validaResposta(topFilmes)
                    || !validaResposta(topSeries)) {

                Log.e("TMDB_WORKER", "Erro ao consultar as listas.");
                return Result.retry();
            }

            Context context = getApplicationContext();

            List<TmdbMovie> listaTendencias = tendencias.body().getResults();
            List<TmdbMovie> listaFilmes = topFilmes.body().getResults();
            List<TmdbMovie> listaSeries = topSeries.body().getResults();

            boolean mudouTendencias = TmdbCache.salvarLista(
                    context, TmdbCache.TENDENCIAS, listaTendencias);

            boolean mudouFilmes = TmdbCache.salvarLista(
                    context, TmdbCache.TOP_FILMES, listaFilmes);

            boolean mudouSeries = TmdbCache.salvarLista(
                    context, TmdbCache.TOP_SERIES, listaSeries);

            List<TmdbReview> reviews = buscarReviews(
                    api, token, listaTendencias, listaFilmes
            );

            TmdbCache.salvarReviews(context, reviews);

            Log.d("TMDB_WORKER", "Tendências alteradas: " + mudouTendencias);
            Log.d("TMDB_WORKER", "Top filmes alterado: " + mudouFilmes);
            Log.d("TMDB_WORKER", "Top séries alterado: " + mudouSeries);
            Log.d("TMDB_WORKER", "Reviews encontrados: " + reviews.size());
            Log.d("TMDB_WORKER", "Atualização concluída!");

            return Result.success();

        } catch (Exception e) {
            Log.e("TMDB_WORKER", "Erro na atualização", e);
            return Result.retry();
        }
    }

    private List<TmdbReview> buscarReviews(
            TmdbApi api,
            String token,
            List<TmdbMovie> tendencias,
            List<TmdbMovie> topFilmes
    ) {
        List<TmdbReview> reviews = new ArrayList<>();
        Set<Integer> filmesConsultados = new HashSet<>();

        List<TmdbMovie> candidatos = new ArrayList<>();
        candidatos.addAll(tendencias);
        candidatos.addAll(topFilmes);

        int filmesPesquisados = 0;

        for (TmdbMovie filme : candidatos) {

            if (reviews.size() >= MAX_REVIEWS
                    || filmesPesquisados >= MAX_MOVIES_TO_SEARCH) {
                break;
            }

            if (!filmesConsultados.add(filme.getId())) {
                continue;
            }

            filmesPesquisados++;

            try {
                Response<TmdbReviewResponse> resposta =
                        api.getMovieReviews(
                                token,
                                filme.getId(),
                                "pt-BR"
                        ).execute();

                if (!resposta.isSuccessful() || resposta.body() == null
                        || resposta.body().getResults() == null) {
                    continue;
                }

                for (TmdbReview review : resposta.body().getResults()) {
                    review.setMovieTitle(filme.getTitle());
                    review.setPosterPath(filme.getPosterPath());
                    reviews.add(review);

                    if (reviews.size() >= MAX_REVIEWS) {
                        break;
                    }
                }

            } catch (Exception e) {
                Log.e(
                        "TMDB_WORKER",
                        "Erro ao buscar reviews do filme: " + filme.getTitle(),
                        e
                );
            }
        }

        return reviews;
    }

    private boolean validaResposta(Response<TmdbResponse> response) {
        return response.isSuccessful()
                && response.body() != null
                && response.body().getResults() != null
                && !response.body().getResults().isEmpty();
    }
}