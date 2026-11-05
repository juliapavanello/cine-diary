package com.example.cinediary.ui.detalhes;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinediary.R;
import com.example.cinediary.data.remote.tmdb.TmdbDetalhes;
import com.example.cinediary.data.repository.DetalhesRepository;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Tela do filme/série: dados vindos da API do TMDB via DetalhesViewModel. */
public class DetalhesFragment extends Fragment {

    public static final String TIPO_FILME = DetalhesRepository.TIPO_FILME;
    public static final String TIPO_SERIE = DetalhesRepository.TIPO_SERIE;

    private static final String ARG_ID = "id";
    private static final String ARG_TIPO = "tipo";
    private static final String URL_BACKDROP = "https://image.tmdb.org/t/p/w780";
    private static final String URL_POSTER = "https://image.tmdb.org/t/p/w342";

    private DetalhesViewModel viewModel;
    private int id;
    private String tipo;

    private View conteudo, layoutErro;
    private ProgressBar progresso;
    private ImageView imgBackdrop, imgPoster;
    private TextView txtTitulo, txtMeta, txtNotaTmdb, txtAvaliacoes, txtPopularidade,
            txtTagline, txtSinopse, txtTituloElenco, txtSemProvedores, txtSemReviews, txtErro;
    private ChipGroup chipsGeneros;
    private RecyclerView rvElenco, rvProvedores, rvReviews;
    private final ElencoAdapter elencoAdapter = new ElencoAdapter();
    private final ProvedorAdapter provedorAdapter = new ProvedorAdapter();
    private final ReviewAdapter reviewAdapter = new ReviewAdapter();

    /** Use sempre este método para abrir a tela. tipo = TIPO_FILME ou TIPO_SERIE. */
    public static DetalhesFragment novaInstancia(int id, String tipo) {
        DetalhesFragment f = new DetalhesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(ARG_TIPO, tipo);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalhes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = requireArguments();
        id = args.getInt(ARG_ID);
        tipo = args.getString(ARG_TIPO, TIPO_FILME);

        conteudo = view.findViewById(R.id.conteudoDetalhes);
        layoutErro = view.findViewById(R.id.layoutErro);
        progresso = view.findViewById(R.id.progressoDetalhes);
        imgBackdrop = view.findViewById(R.id.imgBackdrop);
        imgPoster = view.findViewById(R.id.imgPoster);
        txtTitulo = view.findViewById(R.id.txtTitulo);
        txtMeta = view.findViewById(R.id.txtMeta);
        txtNotaTmdb = view.findViewById(R.id.txtNotaTmdb);
        txtAvaliacoes = view.findViewById(R.id.txtAvaliacoes);
        txtPopularidade = view.findViewById(R.id.txtPopularidade);
        txtSemProvedores = view.findViewById(R.id.txtSemProvedores);
        txtTagline = view.findViewById(R.id.txtTagline);
        txtSinopse = view.findViewById(R.id.txtSinopse);
        txtTituloElenco = view.findViewById(R.id.txtTituloElenco);
        txtErro = view.findViewById(R.id.txtErro);
        chipsGeneros = view.findViewById(R.id.chipsGeneros);
        rvElenco = view.findViewById(R.id.rvElenco);
        rvProvedores = view.findViewById(R.id.rvProvedores);
        rvReviews = view.findViewById(R.id.rvReviews);
        txtSemReviews = view.findViewById(R.id.txtSemReviews);

        rvElenco.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));
        rvElenco.setAdapter(elencoAdapter);

        rvProvedores.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));
        rvProvedores.setAdapter(provedorAdapter);

        rvReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvReviews.setAdapter(reviewAdapter);

        view.findViewById(R.id.btnTentarNovamente).setOnClickListener(v ->
                viewModel.recarregar(id, tipo));

        viewModel = new ViewModelProvider(this).get(DetalhesViewModel.class);
        viewModel.getCarregando().observe(getViewLifecycleOwner(), c ->
                progresso.setVisibility(Boolean.TRUE.equals(c) ? View.VISIBLE : View.GONE));
        viewModel.getErro().observe(getViewLifecycleOwner(), this::mostrarErro);
        viewModel.getDetalhes().observe(getViewLifecycleOwner(), this::mostrarDetalhes);

        viewModel.carregar(id, tipo);
    }

    private void mostrarErro(@Nullable String mensagem) {
        boolean temErro = mensagem != null;
        layoutErro.setVisibility(temErro ? View.VISIBLE : View.GONE);
        if (temErro) {
            txtErro.setText(mensagem);
            conteudo.setVisibility(View.GONE);
        }
    }

    private void mostrarDetalhes(@Nullable TmdbDetalhes d) {
        if (d == null) return;
        conteudo.setVisibility(View.VISIBLE);

        carregarImagem(imgBackdrop, d.getBackdropPath() != null
                ? URL_BACKDROP + d.getBackdropPath() : null);
        carregarImagem(imgPoster, d.getPosterPath() != null
                ? URL_POSTER + d.getPosterPath() : null);

        txtTitulo.setText(d.getTitulo());

        // "2018 - Presente  •  8 temporadas" / "2013  •  1h 52min"
        List<String> meta = new ArrayList<>();
        if (d.getPeriodo() != null) meta.add(d.getPeriodo());
        if (d.getDuracao() != null) meta.add(d.getDuracao());
        txtMeta.setText(TextUtils.join("  •  ", meta));

        txtNotaTmdb.setText(String.format(Locale.getDefault(), "★ %.1f/10", d.getNota()));
        txtAvaliacoes.setText(formatarQuantidade(d.getQuantidadeVotos()));
        txtPopularidade.setText(String.format(Locale.getDefault(), "%.0f", d.getPopularidade()));

        boolean temTagline = d.getTagline() != null && !d.getTagline().isEmpty();
        txtTagline.setVisibility(temTagline ? View.VISIBLE : View.GONE);
        if (temTagline) txtTagline.setText("“" + d.getTagline() + "”");

        boolean temSinopse = d.getSinopse() != null && !d.getSinopse().isEmpty();
        txtSinopse.setText(temSinopse ? d.getSinopse() : getString(R.string.detalhes_sem_sinopse));

        chipsGeneros.removeAllViews();
        for (String genero : d.getNomesGeneros()) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_genero, chipsGeneros, false);
            chip.setText(genero);
            chipsGeneros.addView(chip);
        }

        List<TmdbDetalhes.Provedor> provedores = d.getProvedoresBrasil();
        rvProvedores.setVisibility(provedores.isEmpty() ? View.GONE : View.VISIBLE);
        txtSemProvedores.setVisibility(provedores.isEmpty() ? View.VISIBLE : View.GONE);
        provedorAdapter.setProvedores(provedores);

        List<TmdbDetalhes.Ator> elenco = d.getElenco();
        txtTituloElenco.setVisibility(elenco.isEmpty() ? View.GONE : View.VISIBLE);
        rvElenco.setVisibility(elenco.isEmpty() ? View.GONE : View.VISIBLE);
        elencoAdapter.setAtores(elenco);

        List<TmdbDetalhes.Review> reviews = d.getReviews();
        rvReviews.setVisibility(reviews.isEmpty() ? View.GONE : View.VISIBLE);
        txtSemReviews.setVisibility(reviews.isEmpty() ? View.VISIBLE : View.GONE);
        reviewAdapter.setReviews(reviews);
    }

    /** 950 -> "950", 12345 -> "12,3 mil" */
    private String formatarQuantidade(int n) {
        if (n < 1000) return String.valueOf(n);
        return String.format(Locale.getDefault(), "%.1f mil", n / 1000.0);
    }

    private void carregarImagem(ImageView destino, @Nullable String url) {
        if (url == null) {
            destino.setImageResource(R.drawable.bg_poster_duna);
            return;
        }
        Glide.with(this).load(url)
                .placeholder(R.drawable.bg_poster_duna)
                .error(R.drawable.bg_poster_duna)
                .into(destino);
    }
}
