package com.example.cinediary.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cinediary.R;
import com.example.cinediary.ui.main.MainActivity;

/** Hospeda o NavHostFragment com Boas-vindas -> Login (-> Cadastro). */
public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        AuthViewModel vm = new ViewModelProvider(this).get(AuthViewModel.class);
        if (savedInstanceState == null && vm.temSessaoAtiva()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_auth);
    }
}
