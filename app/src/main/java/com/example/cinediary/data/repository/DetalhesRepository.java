package com.example.cinediary.data.repository;

import androidx.annotation.NonNull;

import com.example.cinediary.data.remote.tmdb.TmdbApi;
import com.example.cinediary.data.remote.tmdb.TmdbClient;
import com.example.cinediary.data.remote.tmdb.TmdbDetalhes;

import retrofit2.Call;
import retrofit2.Response;

/** Model do MVVM: busca os detalhes de um filme ou série na API do TMDB (Retrofit). */
public class DetalhesRepository {

    public static final String TIPO_FILME = "movie";
    public static final String TIPO_SERIE = "tv";

    // Elenco e onde assistir vêm na mesma requisição
    private static final String EXTRAS = "credits,watch/providers,reviews";

    public interface Callback {
        void onSuccess(TmdbDetalhes detalhes);
        void onError(String mensagem);
    }

    public void buscar(int id, String tipo, Callback cb) {
        TmdbApi api = TmdbClient.getApi();
        String token = TmdbClient.getAuthorizationHeader();

        Call<TmdbDetalhes> chamada = TIPO_SERIE.equals(tipo)
                ? api.getTvDetails(token, id, "pt-BR", EXTRAS)
                : api.getMovieDetails(token, id, "pt-BR", EXTRAS);

        // enqueue roda a requisição em segundo plano e devolve o resultado na main thread
        chamada.enqueue(new retrofit2.Callback<TmdbDetalhes>() {
            @Override
            public void onResponse(@NonNull Call<TmdbDetalhes> call,
                                   @NonNull Response<TmdbDetalhes> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TmdbDetalhes detalhes = response.body();
                    if (detalhes.getReviews().isEmpty()) {
                        buscarReviewsEmIngles(api, token, id, tipo, detalhes, cb);
                    } else {
                        cb.onSuccess(detalhes);
                    }
                } else {
                    cb.onError("Não foi possível carregar os detalhes (erro " + response.code() + ").");
                }
            }

            @Override
            public void onFailure(@NonNull Call<TmdbDetalhes> call, @NonNull Throwable t) {
                cb.onError("Sem conexão com a internet.");
            }
        });
    }

    /**
     * A maioria das reviews do TMDB é em inglês: se não veio nenhuma em pt-BR,
     * busca em en-US. Se essa segunda chamada falhar, mostra a tela sem reviews.
     */
    private void buscarReviewsEmIngles(TmdbApi api, String token, int id, String tipo,
                                       TmdbDetalhes detalhes, Callback cb) {
        api.getReviews(token, tipo, id, "en-US").enqueue(new retrofit2.Callback<TmdbDetalhes.Reviews>() {
            @Override
            public void onResponse(@NonNull Call<TmdbDetalhes.Reviews> call,
                                   @NonNull Response<TmdbDetalhes.Reviews> response) {
                if (response.isSuccessful() && response.body() != null) {
                    detalhes.setReviews(response.body().getResults());
                }
                cb.onSuccess(detalhes);
            }

            @Override
            public void onFailure(@NonNull Call<TmdbDetalhes.Reviews> call, @NonNull Throwable t) {
                cb.onSuccess(detalhes);
            }
        });
    }
}
