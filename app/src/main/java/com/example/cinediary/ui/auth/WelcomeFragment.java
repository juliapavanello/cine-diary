package com.example.cinediary.ui.auth;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;

import com.example.cinediary.R;

/** Tela "login 1": carrossel automático + botões Entrar / Criar Conta. */
public class WelcomeFragment extends Fragment {

    private static final long INTERVALO_MS = 4000;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private ViewPager2 pager;

    private final Runnable avancar = new Runnable() {
        @Override
        public void run() {
            if (pager == null || pager.getAdapter() == null) return;
            int total = pager.getAdapter().getItemCount();
            pager.setCurrentItem((pager.getCurrentItem() + 1) % total, true);
            handler.postDelayed(this, INTERVALO_MS);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pager = view.findViewById(R.id.pagerCarrossel);
        TabLayout dots = view.findViewById(R.id.tabDots);

        // Troque pelas suas imagens em res/drawable-nodpi
        pager.setAdapter(new CarouselAdapter(Arrays.asList(
                R.drawable.carrossel_1,
                R.drawable.carrossel_2,
                R.drawable.carrossel_3,
                R.drawable.carrossel_4)));

        // Efeito de "crossfade" entre as imagens em vez de deslizar
        pager.setPageTransformer((page, position) -> {
            page.setTranslationX(-position * page.getWidth());
            page.setAlpha(1f - Math.abs(position));
        });

        new TabLayoutMediator(dots, pager, (tab, pos) -> { }).attach();

        // Se o usuário arrastar manualmente, reinicia o timer
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                handler.removeCallbacks(avancar);
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    handler.postDelayed(avancar, INTERVALO_MS);
                }
            }
        });

        view.findViewById(R.id.btnEntrar).setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_welcome_to_login));

        view.findViewById(R.id.txtCriarConta).setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_welcome_to_cadastro));

        // Afasta o painel inferior da barra de navegação do sistema (edge-to-edge)
        View painel = view.findViewById(R.id.painelInferior);
        int paddingBase = painel.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(painel, (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(),
                    paddingBase + barras.bottom);
            return insets;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(avancar, INTERVALO_MS);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(avancar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(avancar);
        pager = null;
    }
}
