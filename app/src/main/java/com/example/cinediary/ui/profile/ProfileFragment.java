package com.example.cinediary.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinediary.R;

import java.util.Arrays;
import java.util.List;

/** Tela visual de perfil do CineDiary. */
public class ProfileFragment extends Fragment {

    private static final ProfileMockData PROFILE = new ProfileMockData(
            "Jason Maio",
            "Amante de filmes de terror, gosto de pânico, halloween entre outros...",
            "Friday 13th",
            "Cinco anos após o massacre no acampamento Crystal Lake, um novo grupo de jovens chega à região para uma nova temporada de terror...",
            "10,2 mil",
            "★★★★★"
    );

    private static final List<FavoriteMovie> MOCK_FAVORITES = Arrays.asList(
            new FavoriteMovie(1, "Invocação do mal", "Terror   Horror   Mistério",
                    "2h 07min  •  2013\nJá assistido", "★★★★★", R.drawable.poster_conjuring),
            new FavoriteMovie(2, "A Hora do Pesadelo", "Terror   Sobrenatural",
                    "1h 31min  •  1984\nJá assistido", "★★★★☆", R.drawable.poster_nightmare),
            new FavoriteMovie(3, "Friday 13th", "Terror   Suspense",
                    "1h 35min  •  1980\nJá assistido", "★★★★★", R.drawable.poster_friday)
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

        ((TextView) view.findViewById(R.id.tvProfileName)).setText(PROFILE.name);
        ((TextView) view.findViewById(R.id.tvProfileBio)).setText(PROFILE.bio);
        ((TextView) view.findViewById(R.id.tvFavoriteMovieTitle)).setText(PROFILE.favoriteMovieTitle);
        ((TextView) view.findViewById(R.id.tvFavoriteMovieDescription))
                .setText(PROFILE.favoriteMovieDescription);
        ((TextView) view.findViewById(R.id.tvFavoriteMovieRating))
                .setText(PROFILE.favoriteMovieRating);
        ((TextView) view.findViewById(R.id.tvFavoriteMoviePopularity))
                .setText("●  " + PROFILE.favoriteMoviePopularity);

        RecyclerView favoritos = view.findViewById(R.id.favoritesContainer);
        favoritos.setLayoutManager(new LinearLayoutManager(requireContext()));
        favoritos.setNestedScrollingEnabled(false);
        favoritos.setAdapter(new FavoriteMoviesAdapter(MOCK_FAVORITES));
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
}
