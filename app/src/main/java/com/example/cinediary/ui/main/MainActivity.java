
package com.example.cinediary.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinediary.R;
import com.example.cinediary.data.local.SessionPrefs;
import com.example.cinediary.ui.auth.AuthActivity;
import com.example.cinediary.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Exibe a tela inicial do CineDiary
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new HomeFragment())
                    .commit();
        }

        // Mantém o funcionamento do logout
        findViewById(R.id.btnSair).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            new SessionPrefs(this).limpar();

            Intent i = new Intent(this, AuthActivity.class);
            i.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(i);
        });
    }
}
