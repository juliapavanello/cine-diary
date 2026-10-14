package com.example.cinediary.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Camada Model do MVVM: única classe que conversa com o Firebase Auth.
 * ViewModel e telas não importam nada do Firebase diretamente.
 */
public class AuthRepository {

    public interface Callback {
        void onSuccess();
        void onError(Exception e);
    }

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public FirebaseUser getUsuarioAtual() {
        return auth.getCurrentUser();
    }

    public void login(String email, String senha, Callback cb) {
        auth.signInWithEmailAndPassword(email, senha)
                .addOnSuccessListener(r -> cb.onSuccess())
                .addOnFailureListener(cb::onError);
    }

    /** Já deixado pronto para a tela "Criar Conta". */
    public void cadastrar(String nome, String email, String senha, Callback cb) {
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnSuccessListener(r -> {
                    UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nome)
                            .build();
                    r.getUser().updateProfile(perfil)
                            .addOnSuccessListener(v -> cb.onSuccess())
                            .addOnFailureListener(cb::onError);
                })
                .addOnFailureListener(cb::onError);
    }

    public void recuperarSenha(String email, Callback cb) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(v -> cb.onSuccess())
                .addOnFailureListener(cb::onError);
    }

    public void logout() {
        auth.signOut();
    }
}
