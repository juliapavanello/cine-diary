package com.example.cinediary.data.repository;

import androidx.annotation.Nullable;

import com.example.cinediary.data.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Camada Model do MVVM: única classe que conversa com Firebase Auth e Firestore.
 * ViewModels e telas não importam nada do Firebase diretamente.
 */
public class AuthRepository {

    public interface Callback {
        void onSuccess();
        void onError(Exception e);
    }

    private static final String COLECAO_USUARIOS = "usuarios";

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirebaseUser getUsuarioAtual() {
        return auth.getCurrentUser();
    }

    public void login(String email, String senha, Callback cb) {
        auth.signInWithEmailAndPassword(email, senha)
                .addOnSuccessListener(r -> cb.onSuccess())
                .addOnFailureListener(cb::onError);
    }

    /**
     * 1) cria a conta no Auth  2) grava o nome no perfil do Auth
     * 3) salva o documento usuarios/{uid} no Firestore (com a foto, se houver).
     * Se o passo 3 falhar, a conta recém-criada é apagada para não ficar "pela metade".
     */
    public void cadastrar(String nome, String email, String senha,
                          @Nullable String fotoBase64, Callback cb) {
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnSuccessListener(r -> {
                    FirebaseUser user = r.getUser();
                    if (user == null) {
                        cb.onError(new IllegalStateException("Usuário nulo após cadastro"));
                        return;
                    }

                    user.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(nome)
                            .build());

                    db.collection(COLECAO_USUARIOS).document(user.getUid())
                            .set(new Usuario(nome, email, fotoBase64))
                            .addOnSuccessListener(v -> cb.onSuccess())
                            .addOnFailureListener(e ->
                                    user.delete().addOnCompleteListener(t -> cb.onError(e)));
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
