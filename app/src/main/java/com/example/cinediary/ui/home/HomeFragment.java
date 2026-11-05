package com.example.cinediary.ui.home;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinediary.R;
import com.example.cinediary.data.model.HomeData;
import com.example.cinediary.data.remote.tmdb.TmdbMovie;
import com.example.cinediary.data.remote.tmdb.TmdbReview;
import com.example.cinediary.ui.detalhes.DetalhesFragment;
import com.example.cinediary.ui.main.MainActivity;

import java.util.List;

/** View da tela inicial. A obtenção dos dados fica no HomeViewModel/Repository. */
public class HomeFragment extends Fragment {

    private static final int BRANCO = Color.rgb(246, 241, 247);
    private static final int CINZA = Color.rgb(177, 166, 183);
    private static final int ROXO = Color.rgb(217, 75, 255);

    private TrendingMoviesAdapter tendenciasAdapter;
    private HomeViewModel viewModel;

    public HomeFragment() {
        // Construtor vazio exigido pelo FragmentManager.
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView listaTendencias = view.findViewById(R.id.rowTrends);
        LinearLayout topMovies = view.findViewById(R.id.rowTopMovies);
        LinearLayout topSeries = view.findViewById(R.id.rowTopSeries);
        LinearLayout reviews = view.findViewById(R.id.reviewsContainer);

        listaTendencias.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        listaTendencias.setNestedScrollingEnabled(false);

        tendenciasAdapter = new TrendingMoviesAdapter(
                filme -> abrirDetalhes(filme.getId(), DetalhesFragment.TIPO_FILME)
        );
        listaTendencias.setAdapter(tendenciasAdapter);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.getHomeData().observe(getViewLifecycleOwner(), dados -> {
            if (dados != null) {
                atualizarInterface(dados, topMovies, topSeries, reviews);
            }
        });

        // O ViewModel mostra o cache primeiro e solicita a atualização em segundo plano.
        viewModel.carregarHome();
    }

    private void atualizarInterface(HomeData dados,
                                    LinearLayout topMovies,
                                    LinearLayout topSeries,
                                    LinearLayout reviewsContainer) {
        tendenciasAdapter.submitList(dados.getTendencias());
        atualizarRanking(topMovies, dados.getTopFilmes(), DetalhesFragment.TIPO_FILME);
        atualizarRanking(topSeries, dados.getTopSeries(), DetalhesFragment.TIPO_SERIE);

        reviewsContainer.removeAllViews();
        List<TmdbReview> reviews = dados.getReviews();

        if (reviews.isEmpty()) {
            reviewsContainer.addView(text(
                    "Nenhuma opinião encontrada.", 11, CINZA, false
            ));
            return;
        }

        for (int i = 0; i < Math.min(5, reviews.size()); i++) {
            addReviewTmdb(reviewsContainer, reviews.get(i));
        }
    }

    private void atualizarRanking(LinearLayout container, List<TmdbMovie> filmes, String tipo) {
        container.removeAllViews();
        for (int i = 0; i < Math.min(10, filmes.size()); i++) {
            addRankingTmdb(container, i + 1, filmes.get(i), tipo);
        }
    }

    /** Abre a tela do filme/série pela MainActivity. */
    private void abrirDetalhes(int id, String tipo) {
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).abrirDetalhes(id, tipo);
        }
    }

    private void addRankingTmdb(LinearLayout container, int rank, TmdbMovie filme, String tipo) {
        FrameLayout tile = new FrameLayout(requireContext());
        tile.setOnClickListener(v -> abrirDetalhes(filme.getId(), tipo));
        LinearLayout.LayoutParams tileParams = new LinearLayout.LayoutParams(dp(176), dp(135));
        tileParams.setMarginEnd(dp(10));
        tile.setLayoutParams(tileParams);
        tile.setBackgroundColor(Color.rgb(13, 11, 14));

        TextView number = text(String.valueOf(rank), 66, ROXO, true);
        number.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        FrameLayout.LayoutParams numberParams = new FrameLayout.LayoutParams(
                dp(88), ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.START | Gravity.CENTER_VERTICAL
        );
        tile.addView(number, numberParams);

        ImageView poster = new ImageView(requireContext());
        poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        FrameLayout.LayoutParams posterParams = new FrameLayout.LayoutParams(
                dp(96), dp(135), Gravity.END | Gravity.CENTER_VERTICAL
        );
        posterParams.rightMargin = dp(3);
        tile.addView(poster, posterParams);

        String posterPath = filme.getPosterPath();
        if (posterPath != null && !posterPath.isEmpty()) {
            String url = "https://image.tmdb.org/t/p/w500" + posterPath;
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.bg_poster_duna)
                    .error(R.drawable.bg_poster_duna)
                    .into(poster);
        } else {
            poster.setImageResource(R.drawable.bg_poster_duna);
        }

        container.addView(tile);
    }

    private void addReviewTmdb(LinearLayout container, TmdbReview review) {
        LinearLayout card = new LinearLayout(requireContext());
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(9), dp(9), dp(10), dp(9));
        card.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_review_card));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.bottomMargin = dp(9);
        card.setLayoutParams(cardParams);

        ImageView poster = new ImageView(requireContext());
        poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams posterParams = new LinearLayout.LayoutParams(dp(48), dp(61));
        posterParams.setMarginEnd(dp(10));
        card.addView(poster, posterParams);

        String posterPath = review.getPosterPath();
        if (posterPath != null && !posterPath.isEmpty()) {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500" + posterPath)
                    .placeholder(R.drawable.bg_poster_duna)
                    .error(R.drawable.bg_poster_duna)
                    .into(poster);
        } else {
            poster.setImageResource(R.drawable.bg_poster_duna);
        }

        LinearLayout body = new LinearLayout(requireContext());
        body.setOrientation(LinearLayout.VERTICAL);
        body.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
        ));

        TextView titulo = text(
                review.getMovieTitle() != null ? review.getMovieTitle() : "Filme",
                10, ROXO, true
        );
        titulo.setMaxLines(1);
        titulo.setEllipsize(android.text.TextUtils.TruncateAt.END);
        body.addView(titulo);

        TextView autor = text(
                "Por " + (review.getAuthor() != null ? review.getAuthor() : "Anônimo"),
                10, BRANCO, true
        );
        LinearLayout.LayoutParams autorParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        autorParams.topMargin = dp(3);
        body.addView(autor, autorParams);

        TextView comentario = text(
                review.getContent() != null ? review.getContent() : "",
                9, CINZA, false
        );
        comentario.setMaxLines(3);
        comentario.setEllipsize(android.text.TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams comentarioParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        comentarioParams.topMargin = dp(5);
        body.addView(comentario, comentarioParams);

        card.addView(body);
        container.addView(card);
    }

    private TextView text(String value, int sp, int color, boolean bold) {
        TextView textView = new TextView(requireContext());
        textView.setText(value);
        textView.setTextSize(sp);
        textView.setTextColor(color);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setIncludeFontPadding(false);
        if (bold) textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return textView;
    }

    private int dp(float value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
