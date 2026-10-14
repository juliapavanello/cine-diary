package com.example.cinediary.ui.auth;

/**
 * Estado que o ViewModel expõe para a tela.
 * "consumir()" evita que um Toast ou navegação se repita ao girar a tela.
 */
public class AuthState {

    public enum Status { CARREGANDO, SUCESSO, ERRO, EMAIL_ENVIADO }
    public enum Campo { NENHUM, NOME, EMAIL, SENHA, CONFIRMAR_SENHA }

    public final Status status;
    public final Campo campo;
    public final String mensagem;
    private boolean consumido = false;

    private AuthState(Status status, Campo campo, String mensagem) {
        this.status = status;
        this.campo = campo;
        this.mensagem = mensagem;
    }

    public static AuthState carregando() { return new AuthState(Status.CARREGANDO, Campo.NENHUM, null); }
    public static AuthState sucesso()    { return new AuthState(Status.SUCESSO, Campo.NENHUM, null); }
    public static AuthState emailEnviado() {
        return new AuthState(Status.EMAIL_ENVIADO, Campo.NENHUM,
                "Enviamos um link de redefinição para o seu e-mail.");
    }
    public static AuthState erro(Campo campo, String msg) { return new AuthState(Status.ERRO, campo, msg); }

    /** Retorna true só na primeira vez que é chamado. */
    public boolean consumir() {
        if (consumido) return false;
        consumido = true;
        return true;
    }
}
