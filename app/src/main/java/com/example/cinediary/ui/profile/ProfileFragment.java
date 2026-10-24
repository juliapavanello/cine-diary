package com.example.cinediary.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cinediary.R;

import java.util.Arrays;
import java.util.List;

/**
 * Tela visual de perfil do CineDiary.
 * Os dados abaixo são mockados e podem ser substituídos por dados do Firestore depois.
 */
public class ProfileFragment extends Fragment {

    private static final ProfileMockData PROFILE = new ProfileMockData(
            "Jason Maio",
            "Amante de filmes de terror, gosto de pânico, halloween entre outros...",
            "Friday 13th",
            "Cinco anos após o massacre no acampamento Crystal Lake, um novo grupo de jovens chega à região para uma nova temporada de terror...",
            "10,2 mil",
            "★★★★★"
    );

    private static final List<FavoriteItem> MOCK_FAVORITES = Arrays.asList(
            new FavoriteItem(1, "Invocação do mal", "Terror   Horror   Mistério", "2h 07min  •  2013\nJá assistido", "★★★★★", R.drawable.poster_conjuring),
            new FavoriteItem(2, "A Hora do Pesadelo", "Terror   Sobrenatural", "1h 31min  •  1984\nJá assistido", "★★★★☆", R.drawable.poster_nightmare),
            new FavoriteItem(3, "Friday 13th", "Terror   Suspense", "1h 35min  •  1980\nJá assistido", "★★★★★", R.drawable.poster_friday)
    );

    public ProfileFragment() {
        // Construtor público sem argumentos necessário para o FragmentManager.
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Preenche os dados simples do perfil a partir do objeto de exemplo.
        ((TextView) view.findViewById(R.id.tvProfileName)).setText(PROFILE.name);
        ((TextView) view.findViewById(R.id.tvProfileBio)).setText(PROFILE.bio);
        ((TextView) view.findViewById(R.id.tvFavoriteMovieTitle)).setText(PROFILE.favoriteMovieTitle);
        ((TextView) view.findViewById(R.id.tvFavoriteMovieDescription)).setText(PROFILE.favoriteMovieDescription);
        ((TextView) view.findViewById(R.id.tvFavoriteMovieRating)).setText(PROFILE.favoriteMovieRating);
        ((TextView) view.findViewById(R.id.tvFavoriteMoviePopularity))
                .setText("●  " + PROFILE.favoriteMoviePopularity);

        renderFavorites(view);
    }

    /**
     * Substitui as linhas-exemplo do XML por linhas montadas com dados fixos.
     * O XML mantém os exemplos para que a tela também seja visível na prévia do editor.
     */
    private void renderFavorites(@NonNull View root) {
        LinearLayout container = root.findViewById(R.id.favoritesContainer);
        container.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (FavoriteItem item : MOCK_FAVORITES) {
            View row = inflater.inflate(R.layout.item_profile_favorite, container, false);

            ((TextView) row.findViewById(R.id.tvFavoriteRank)).setText(String.valueOf(item.position));
            ((ImageView) row.findViewById(R.id.ivFavoritePoster)).setImageResource(item.posterResource);
            ((TextView) row.findViewById(R.id.tvFavoriteTitle)).setText(item.title);
            ((TextView) row.findViewById(R.id.tvFavoriteGenres)).setText(item.genres);
            ((TextView) row.findViewById(R.id.tvFavoriteMeta)).setText(item.metadata);
            ((TextView) row.findViewById(R.id.tvFavoriteRating)).setText(item.rating);

            container.addView(row);
        }
    }

    private static class ProfileMockData {
        final String name;
        final String bio;
        final String favoriteMovieTitle;
        final String favoriteMovieDescription;
        final String favoriteMoviePopularity;
        final String favoriteMovieRating;

        ProfileMockData(String name, String bio, String favoriteMovieTitle,
                        String favoriteMovieDescription, String favoriteMoviePopularity,
                        String favoriteMovieRating) {
            this.name = name;
            this.bio = bio;
            this.favoriteMovieTitle = favoriteMovieTitle;
            this.favoriteMovieDescription = favoriteMovieDescription;
            this.favoriteMoviePopularity = favoriteMoviePopularity;
            this.favoriteMovieRating = favoriteMovieRating;
        }
    }

    private static class FavoriteItem {
        final int position;
        final String title;
        final String genres;
        final String metadata;
        final String rating;
        final int posterResource;

        FavoriteItem(int position, String title, String genres, String metadata,
                     String rating, int posterResource) {
            this.position = position;
            this.title = title;
            this.genres = genres;
            this.metadata = metadata;
            this.rating = rating;
            this.posterResource = posterResource;
        }
    }
}
