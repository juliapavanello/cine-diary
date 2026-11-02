
package com.example.cinediary.data.remote.tmdb;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class TmdbUpdateScheduler {

    public static final String NOME_TRABALHO_INICIAL =
            "tmdb_initial_update";

    public static void agendar(Context context) {

        Context appContext = context.getApplicationContext();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Atualização periódica
        PeriodicWorkRequest periodico =
                new PeriodicWorkRequest.Builder(
                        TmdbUpdateWorker.class,
                        15,
                        TimeUnit.MINUTES
                )
                        .setConstraints(constraints)
                        .build();

        WorkManager manager = WorkManager.getInstance(appContext);

        manager.enqueueUniquePeriodicWork(
                "tmdb_periodic_update",
                ExistingPeriodicWorkPolicy.KEEP,
                periodico
        );

        // Busca inicial caso ainda não existam as três listas
        if (!TmdbCache.temTodasListas(appContext)) {

            OneTimeWorkRequest inicial =
                    new OneTimeWorkRequest.Builder(
                            TmdbUpdateWorker.class
                    )
                            .setConstraints(constraints)
                            .build();

            manager.enqueueUniqueWork(
                    NOME_TRABALHO_INICIAL,
                    ExistingWorkPolicy.KEEP,
                    inicial
            );
        }
    }
}
