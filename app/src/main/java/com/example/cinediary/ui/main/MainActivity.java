package com.example.cinediary.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.cinediary.R;
import com.example.cinediary.data.local.SessionPrefs;
import com.example.cinediary.ui.auth.AuthActivity;
import com.example.cinediary.ui.home.HomeFragment;
import com.example.cinediary.ui.profile.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_DESTINO_ATUAL = "destino_atual";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private int destinoAtual = R.id.nav_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START)
        );

        navigationView.setNavigationItemSelectedListener(this::onItemSelecionado);

        if (savedInstanceState == null) {
            mostrarHome();
        } else {
            destinoAtual = savedInstanceState.getInt(
                    KEY_DESTINO_ATUAL,
                    R.id.nav_home
            );
            navigationView.setCheckedItem(destinoAtual);
            atualizarTitulo(destinoAtual);
        }

        configurarBotaoVoltar();
    }

    private boolean onItemSelecionado(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            mostrarHome();
        } else if (id == R.id.nav_profile) {
            mostrarPerfil();
        } else if (id == R.id.nav_logout) {
            deslogar();
            return true;
        } else {
            Toast.makeText(
                    this,
                    "Essa opção estará disponível em breve!",
                    Toast.LENGTH_SHORT
            ).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void mostrarHome() {
        abrirFragment(new HomeFragment(), R.id.nav_home, "CineDiary");
    }

    private void mostrarPerfil() {
        abrirFragment(new ProfileFragment(), R.id.nav_profile, "Meu perfil");
    }

    private void abrirFragment(Fragment fragment, int destino, String titulo) {
        if (destinoAtual != destino || getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
        }

        destinoAtual = destino;
        navigationView.setCheckedItem(destino);
        toolbar.setTitle(titulo);
    }

    private void atualizarTitulo(int destino) {
        if (destino == R.id.nav_profile) {
            toolbar.setTitle("Meu perfil");
        } else {
            toolbar.setTitle("CineDiary");
        }
    }

    private void deslogar() {
        FirebaseAuth.getInstance().signOut();
        new SessionPrefs(this).limpar();

        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);
        finish();
    }

    private void configurarBotaoVoltar() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (destinoAtual != R.id.nav_home) {
                    mostrarHome();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_DESTINO_ATUAL, destinoAtual);
        super.onSaveInstanceState(outState);
    }
}
