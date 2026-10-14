package com.example.cinediary.data.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Guarda só a preferência "manter conectado".
 * A senha NUNCA é salva: quem mantém a sessão é o próprio Firebase Auth.
 */
public class SessionPrefs {

    private static final String ARQUIVO = "sessao";
    private static final String KEY_MANTER_CONECTADO = "manter_conectado";

    private final SharedPreferences prefs;

    public SessionPrefs(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
    }

    public boolean isManterConectado() {
        return prefs.getBoolean(KEY_MANTER_CONECTADO, false);
    }

    public void setManterConectado(boolean manter) {
        prefs.edit().putBoolean(KEY_MANTER_CONECTADO, manter).apply();
    }

    public void limpar() {
        prefs.edit().clear().apply();
    }
}
