package com.example.cinediary.ui.detalhes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cinediary.data.remote.tmdb.TmdbDetalhes;
import com.example.cinediary.data.repository.DetalhesRepository;

/** ViewModel da tela do filme: pede os dados ao repositório e expõe para a tela. */
public class DetalhesViewModel extends ViewModel {

    private final DetalhesRepository repo = new DetalhesRepository();

    private final MutableLiveData<TmdbDetalhes> detalhes = new MutableLiveData<>();
    private final MutableLiveData<Boolean> carregando = new MutableLiveData<>(false);
    private final MutableLiveData<String> erro = new MutableLiveData<>();

    public LiveData<TmdbDetalhes> getDetalhes() { return detalhes; }
    public LiveData<Boolean> getCarregando() { return carregando; }
    public LiveData<String> getErro() { return erro; }

    /** Só busca se ainda não tem os dados (girar a tela não refaz a requisição). */
    public void carregar(int id, String tipo) {
        if (detalhes.getValue() != null || Boolean.TRUE.equals(carregando.getValue())) return;
        recarregar(id, tipo);
    }

    public void recarregar(int id, String tipo) {
        carregando.setValue(true);
        erro.setValue(null);
        repo.buscar(id, tipo, new DetalhesRepository.Callback() {
            @Override public void onSuccess(TmdbDetalhes d) {
                carregando.setValue(false);
                detalhes.setValue(d);
            }
            @Override public void onError(String mensagem) {
                carregando.setValue(false);
                erro.setValue(mensagem);
            }
        });
    }
}
