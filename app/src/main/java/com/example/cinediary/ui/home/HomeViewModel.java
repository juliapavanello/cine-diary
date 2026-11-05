package com.example.cinediary.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.work.WorkInfo;

import com.example.cinediary.data.model.HomeData;
import com.example.cinediary.data.repository.HomeRepository;

import java.util.List;

/** ViewModel da Home: prepara os dados e acompanha a atualização em segundo plano. */
public class HomeViewModel extends AndroidViewModel {

    private final HomeRepository repository;
    private final MediatorLiveData<HomeData> homeData = new MediatorLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new HomeRepository(application);

        homeData.addSource(repository.observarAtualizacaoInicial(), workInfos -> {
            if (workInfos == null) return;

            for (WorkInfo info : workInfos) {
                if (info.getState() == WorkInfo.State.SUCCEEDED) {
                    homeData.setValue(repository.obterDadosDoCache());
                    break;
                }
            }
        });
    }

    public LiveData<HomeData> getHomeData() {
        return homeData;
    }

    /** Mostra o cache imediatamente e agenda a consulta quando necessário. */
    public void carregarHome() {
        homeData.setValue(repository.obterDadosDoCache());
        repository.agendarAtualizacao();
    }
}
