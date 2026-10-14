package com.example.cinediary.util;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestoreException;

/** Traduz as exceções do Firebase Auth para mensagens em português. */
public final class FirebaseErros {

    private FirebaseErros() {}

    public static String traduzir(Exception e) {
        // WeakPassword herda de InvalidCredentials, por isso vem antes
        if (e instanceof FirebaseAuthWeakPasswordException)
            return "Senha fraca. Use pelo menos 6 caracteres.";
        if (e instanceof FirebaseAuthInvalidCredentialsException)
            return "E-mail ou senha incorretos.";
        if (e instanceof FirebaseAuthInvalidUserException)
            return "Conta não encontrada ou desativada.";
        if (e instanceof FirebaseAuthUserCollisionException)
            return "Já existe uma conta com esse e-mail.";
        if (e instanceof FirebaseNetworkException)
            return "Sem conexão com a internet.";
        if (e instanceof FirebaseTooManyRequestsException)
            return "Muitas tentativas. Aguarde um pouco e tente de novo.";
        if (e instanceof FirebaseFirestoreException
                && ((FirebaseFirestoreException) e).getCode()
                   == FirebaseFirestoreException.Code.PERMISSION_DENIED)
            return "Sem permissão para salvar os dados (confira as regras do Firestore).";
        return "Não foi possível concluir. Tente novamente.";
    }
}
