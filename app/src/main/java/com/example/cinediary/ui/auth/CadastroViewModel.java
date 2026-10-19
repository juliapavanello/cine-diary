package com.example.cinediary.ui.auth;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.cinediary.data.local.SessionPrefs;
import com.example.cinediary.data.repository.AuthRepository;
import com.example.cinediary.util.FirebaseErros;
import com.example.cinediary.util.ImagemUtils;

/** ViewModel da tela de cadastro: foto de perfil + validação + criação da conta. */
public class CadastroViewModel extends AndroidViewModel {

    private static final int LADO_FOTO = 256; // px

    private final AuthRepository repo = new AuthRepository();
    private final SessionPrefs prefs;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<AuthState> estado = new MutableLiveData<>();
    private final MutableLiveData<Bitmap> foto = new MutableLiveData<>();
    private volatile String fotoBase64; // null = sem foto

    public CadastroViewModel(@NonNull Application app) {
        super(app);
        prefs = new SessionPrefs(app);
    }

    public LiveData<AuthState> getEstado() { return estado; }
    public LiveData<Bitmap> getFoto() { return foto; }
    public boolean temFoto() { return fotoBase64 != null; }

    /** Processa a imagem (câmera ou galeria) em segundo plano. */
    public void selecionarFoto(Uri uri) {
        if (uri == null) return;
        executor.execute(() -> {
            try {
                Bitmap bmp = ImagemUtils.carregarQuadrado(getApplication(), uri, LADO_FOTO);
                fotoBase64 = ImagemUtils.paraBase64(bmp);
                foto.postValue(bmp);
            } catch (Exception e) {
                estado.postValue(AuthState.erro(AuthState.Campo.NENHUM,
                        "Não foi possível carregar a imagem."));
            }
        });
    }

    public void removerFoto() {
        fotoBase64 = null;
        foto.setValue(null);
    }

    public void cadastrar(String nome, String email, String senha, String confirmacao) {
        nome = nome.trim();
        email = email.trim();

        if (nome.isEmpty()) {
            estado.setValue(AuthState.erro(AuthState.Campo.NOME, "Informe seu nome."));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            estado.setValue(AuthState.erro(AuthState.Campo.EMAIL, "E-mail inválido."));
            return;
        }
        if (senha.length() < 6) {
            estado.setValue(AuthState.erro(AuthState.Campo.SENHA, "Use pelo menos 6 caracteres."));
            return;
        }
        if (!senha.equals(confirmacao)) {
            estado.setValue(AuthState.erro(AuthState.Campo.CONFIRMAR_SENHA, "As senhas não coincidem."));
            return;
        }

        estado.setValue(AuthState.carregando());
        repo.cadastrar(nome, email, senha, fotoBase64, new AuthRepository.Callback() {
            @Override public void onSuccess() {
                prefs.setManterConectado(true); // acabou de criar a conta: mantém logado
                estado.setValue(AuthState.sucesso());
            }
            @Override public void onError(Exception e) {
                estado.setValue(AuthState.erro(AuthState.Campo.NENHUM, FirebaseErros.traduzir(e)));
            }
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
