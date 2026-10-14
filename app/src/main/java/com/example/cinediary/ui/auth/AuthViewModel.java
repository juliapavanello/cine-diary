package com.example.cinediary.ui.auth;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cinediary.data.local.SessionPrefs;
import com.example.cinediary.data.repository.AuthRepository;
import com.example.cinediary.util.FirebaseErros;

/** ViewModel do MVVM: valida os campos, chama o repositório e publica o estado. */
public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository repo = new AuthRepository();
    private final SessionPrefs prefs;
    private final MutableLiveData<AuthState> estado = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application app) {
        super(app);
        prefs = new SessionPrefs(app);
    }

    public LiveData<AuthState> getEstado() {
        return estado;
    }

    /**
     * Firebase mantém a sessão sozinho. Se o usuário NÃO marcou "manter conectado",
     * encerramos a sessão quando o app é aberto de novo.
     */
    public boolean temSessaoAtiva() {
        if (repo.getUsuarioAtual() == null) return false;
        if (prefs.isManterConectado()) return true;
        repo.logout();
        return false;
    }

    public void login(String email, String senha, boolean manterConectado) {
        email = email.trim();
        if (email.isEmpty()) {
            estado.setValue(AuthState.erro(AuthState.Campo.EMAIL, "Informe seu e-mail."));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            estado.setValue(AuthState.erro(AuthState.Campo.EMAIL, "E-mail inválido."));
            return;
        }
        if (senha.isEmpty()) {
            estado.setValue(AuthState.erro(AuthState.Campo.SENHA, "Informe sua senha."));
            return;
        }

        estado.setValue(AuthState.carregando());
        repo.login(email, senha, new AuthRepository.Callback() {
            @Override public void onSuccess() {
                prefs.setManterConectado(manterConectado);
                estado.setValue(AuthState.sucesso());
            }
            @Override public void onError(Exception e) {
                estado.setValue(AuthState.erro(AuthState.Campo.NENHUM, FirebaseErros.traduzir(e)));
            }
        });
    }

    public void recuperarSenha(String email) {
        email = email.trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            estado.setValue(AuthState.erro(AuthState.Campo.EMAIL,
                    "Digite seu e-mail acima para recuperar a senha."));
            return;
        }
        estado.setValue(AuthState.carregando());
        repo.recuperarSenha(email, new AuthRepository.Callback() {
            @Override public void onSuccess() { estado.setValue(AuthState.emailEnviado()); }
            @Override public void onError(Exception e) {
                estado.setValue(AuthState.erro(AuthState.Campo.NENHUM, FirebaseErros.traduzir(e)));
            }
        });
    }
}
