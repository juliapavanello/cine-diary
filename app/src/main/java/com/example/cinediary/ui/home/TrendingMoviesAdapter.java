package com.example.cinediary.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinediary.R;
import com.example.cinediary.data.remote.tmdb.TmdbMovie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Adapter da lista horizontal de filmes em tendência da Home. */
public class TrendingMoviesAdapter
        extends RecyclerView.Adapter<TrendingMoviesAdapter.MovieViewHolder> {

    /** Avisa a tela quando um filme da lista é tocado. */
    public interface OnMovieClickListener {
        void onMovieClick(TmdbMovie filme);
    }

    private final List<TmdbMovie> filmes = new ArrayList<>();
    private final OnMovieClickListener listener;

    public TrendingMoviesAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tmdb_trending, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(filmes.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return filmes.size();
    }

    public void submitList(List<TmdbMovie> novaLista) {
        filmes.clear();
        if (novaLista != null) filmes.addAll(novaLista);
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ImageView poster;
        private final TextView titulo;
        private final TextView nota;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.ivTrendingPoster);
            titulo = itemView.findViewById(R.id.tvTrendingTitle);
            nota = itemView.findViewById(R.id.tvTrendingRating);
        }

        void bind(TmdbMovie filme, OnMovieClickListener listener) {
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onMovieClick(filme);
            });
            titulo.setText(filme.getTitle() != null ? filme.getTitle() : "Sem título");
            nota.setText(String.format(Locale.getDefault(), "%.1f/10", filme.getVoteAverage()));

            String posterPath = filme.getPosterPath();
            if (posterPath != null && !posterPath.isEmpty()) {
                String url = "https://image.tmdb.org/t/p/w500" + posterPath;
                Glide.with(itemView.getContext())
                        .load(url)
                        .placeholder(R.drawable.bg_poster_duna)
                        .error(R.drawable.bg_poster_duna)
                        .into(poster);
            } else {
                poster.setImageResource(R.drawable.bg_poster_duna);
            }
        }
    }
}
