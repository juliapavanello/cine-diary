package com.example.cinediary.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.cinediary.R;
import com.example.cinediary.data.local.SessionPrefs;
import com.example.cinediary.ui.auth.AuthActivity;

/**
 * Placeholder da área logada, só para testar o login.
 * Próximo passo: trocar por BottomNavigationView + Fragments (Home, Busca, Perfil...).
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        TextView txt = findViewById(R.id.txtOla);
        txt.setText(usuario != null ? "Logada como " + usuario.getEmail() : "Sem sessão");

        findViewById(R.id.btnSair).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            new SessionPrefs(this).limpar();
            Intent i = new Intent(this, AuthActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
    }
}
