package com.example.cinediary.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinediary.R;

import java.util.ArrayList;
import java.util.List;

/** Adapter da lista vertical de filmes favoritos do Perfil. */
public class FavoriteMoviesAdapter
        extends RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteViewHolder> {

    private final List<FavoriteMovie> favoritos = new ArrayList<>();

    public FavoriteMoviesAdapter(List<FavoriteMovie> favoritos) {
        if (favoritos != null) this.favoritos.addAll(favoritos);
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.bind(favoritos.get(position));
    }

    @Override
    public int getItemCount() {
        return favoritos.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private final TextView rank;
        private final ImageView poster;
        private final TextView title;
        private final TextView genres;
        private final TextView metadata;
        private final TextView rating;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.tvFavoriteRank);
            poster = itemView.findViewById(R.id.ivFavoritePoster);
            title = itemView.findViewById(R.id.tvFavoriteTitle);
            genres = itemView.findViewById(R.id.tvFavoriteGenres);
            metadata = itemView.findViewById(R.id.tvFavoriteMeta);
            rating = itemView.findViewById(R.id.tvFavoriteRating);
        }

        void bind(FavoriteMovie movie) {
            rank.setText(String.valueOf(movie.getPosition()));
            poster.setImageResource(movie.getPosterResource());
            title.setText(movie.getTitle());
            genres.setText(movie.getGenres());
            metadata.setText(movie.getMetadata());
            rating.setText(movie.getRating());
        }
    }
}
