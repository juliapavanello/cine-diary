package com.example.cinediary.ui.home;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cinediary.R;

/**
 * Tela inicial visual do CineDiary.
 * Os dados abaixo são mockados; podem ser substituídos por dados do Firestore depois.
 */
public class HomeFragment extends Fragment {

    private static final int BRANCO = Color.rgb(246, 241, 247);
    private static final int CINZA = Color.rgb(177, 166, 183);
    private static final int ROXO = Color.rgb(217, 75, 255);
    private static final int FUNDO = Color.rgb(13, 11, 14);

    public HomeFragment() {
        // Construtor vazio exigido pelo FragmentManager.
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout trends = view.findViewById(R.id.rowTrends);
        LinearLayout topMovies = view.findViewById(R.id.rowTopMovies);
        LinearLayout topSeries = view.findViewById(R.id.rowTopSeries);
        LinearLayout reviews = view.findViewById(R.id.reviewsContainer);

        // Tendências da semana — dados de exemplo.
        addTrend(trends, new Poster("Frankenstein", "4,8/5", R.drawable.bg_poster_frankenstein, Color.WHITE));
        addTrend(trends, new Poster("Pluribus", "4,5/5", R.drawable.bg_poster_pluribus, Color.rgb(38, 25, 22)));
        addTrend(trends, new Poster("Stranger Things", "4,7/5", R.drawable.bg_poster_stranger, Color.rgb(255, 210, 215)));
        addTrend(trends, new Poster("Duna: Parte Dois", "4,6/5", R.drawable.bg_poster_duna, Color.WHITE));

        // Rankings de filmes — o número grande substitui elementos decorativos/ícones.
        addRanking(topMovies, 1, new Poster("Duna: Parte Dois", "", R.drawable.bg_poster_duna, Color.WHITE));
        addRanking(topMovies, 2, new Poster("Interestelar", "", R.drawable.bg_poster_interstellar, Color.WHITE));
        addRanking(topMovies, 3, new Poster("Oppenheimer", "", R.drawable.bg_poster_oppenheimer, Color.WHITE));
        addRanking(topMovies, 4, new Poster("Frankenstein", "", R.drawable.bg_poster_frankenstein, Color.WHITE));

        // Rankings de séries.
        addRanking(topSeries, 1, new Poster("Pluribus", "", R.drawable.bg_poster_pluribus, Color.rgb(38, 25, 22)));
        addRanking(topSeries, 2, new Poster("Stranger Things", "", R.drawable.bg_poster_stranger, Color.rgb(255, 210, 215)));
        addRanking(topSeries, 3, new Poster("Ruptura", "", R.drawable.bg_poster_ruptura, Color.WHITE));
        addRanking(topSeries, 4, new Poster("Dark", "", R.drawable.bg_poster_interstellar, Color.WHITE));

        // Últimas opiniões — texto estático por enquanto.
        addReview(reviews, "PLURIBUS", "Amanda", "4,5/5",
                "Uma história intrigante, com personagens marcantes e um clima que prende até o fim.",
                new Poster("P", "", R.drawable.bg_poster_pluribus, Color.rgb(38, 25, 22)));
        addReview(reviews, "FRANKENSTEIN", "Fernando", "4,8/5",
                "Visual incrível e uma interpretação marcante. Uma das melhores experiências do ano.",
                new Poster("F", "", R.drawable.bg_poster_frankenstein, Color.WHITE));
    }

    private void addTrend(LinearLayout container, Poster poster) {
        LinearLayout card = new LinearLayout(requireContext());
        card.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(dp(104),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMarginEnd(dp(12));
        card.setLayoutParams(cardParams);

        card.addView(createPoster(poster, dp(104), dp(140), true));

        TextView title = text(poster.title, 11, BRANCO, true);
        title.setMaxLines(1);
        title.setEllipsize(android.text.TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.topMargin = dp(5);
        card.addView(title, titleParams);

        TextView rating = text(poster.rating, 11, ROXO, true);
        LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ratingParams.topMargin = dp(2);
        card.addView(rating, ratingParams);
        container.addView(card);
    }

    private void addRanking(LinearLayout container, int rank, Poster poster) {
        FrameLayout tile = new FrameLayout(requireContext());
        LinearLayout.LayoutParams tileParams = new LinearLayout.LayoutParams(dp(176), dp(135));
        tileParams.setMarginEnd(dp(10));
        tile.setLayoutParams(tileParams);
        tile.setBackgroundColor(FUNDO);

        TextView number = text(String.valueOf(rank), 66, ROXO, true);
        number.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        number.setIncludeFontPadding(false);
        FrameLayout.LayoutParams numberParams = new FrameLayout.LayoutParams(dp(88),
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.START | Gravity.CENTER_VERTICAL);
        numberParams.leftMargin = dp(1);
        tile.addView(number, numberParams);

        FrameLayout posterView = createPoster(poster, dp(96), dp(135), true);
        FrameLayout.LayoutParams posterParams = new FrameLayout.LayoutParams(dp(96), dp(135),
                Gravity.END | Gravity.CENTER_VERTICAL);
        posterParams.rightMargin = dp(3);
        tile.addView(posterView, posterParams);
        container.addView(tile);
    }

    private FrameLayout createPoster(Poster poster, int width, int height, boolean showTitle) {
        FrameLayout frame = new FrameLayout(requireContext());
        frame.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        frame.setBackground(ContextCompat.getDrawable(requireContext(), poster.backgroundRes));
        frame.setClipToOutline(true);
        frame.setElevation(dp(1));

        if (showTitle) {
            TextView artworkTitle = text(poster.artTitle == null || poster.artTitle.isEmpty()
                    ? poster.title.toUpperCase() : poster.artTitle, width <= dp(100) ? 12 : 11,
                    poster.titleColor, true);
            artworkTitle.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            artworkTitle.setPadding(dp(4), dp(4), dp(4), dp(10));
            artworkTitle.setMaxLines(3);
            artworkTitle.setShadowLayer(4f, 0f, 1f, Color.BLACK);
            FrameLayout.LayoutParams labelParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frame.addView(artworkTitle, labelParams);
        }
        return frame;
    }

    private void addReview(LinearLayout container, String mediaTitle, String user, String rating,
                           String message, Poster poster) {
        LinearLayout card = new LinearLayout(requireContext());
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(9), dp(9), dp(10), dp(9));
        card.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_review_card));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.bottomMargin = dp(9);
        card.setLayoutParams(cardParams);

        FrameLayout thumbnail = createPoster(poster, dp(48), dp(61), false);
        LinearLayout.LayoutParams thumbParams = new LinearLayout.LayoutParams(dp(48), dp(61));
        thumbParams.setMarginEnd(dp(10));
        card.addView(thumbnail, thumbParams);

        LinearLayout body = new LinearLayout(requireContext());
        body.setOrientation(LinearLayout.VERTICAL);
        body.setLayoutParams(new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView heading = text(mediaTitle + "  ·  " + rating, 10, ROXO, true);
        heading.setMaxLines(1);
        heading.setEllipsize(android.text.TextUtils.TruncateAt.END);
        body.addView(heading);

        TextView author = text(user, 10, BRANCO, true);
        LinearLayout.LayoutParams authorParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        authorParams.topMargin = dp(2);
        body.addView(author, authorParams);

        TextView comment = text(message, 9, CINZA, false);
        comment.setMaxLines(2);
        comment.setEllipsize(android.text.TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams commentParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        commentParams.topMargin = dp(4);
        body.addView(comment, commentParams);

        card.addView(body);
        container.addView(card);
    }

    private TextView text(String value, int sp, int color, boolean bold) {
        TextView view = new TextView(requireContext());
        view.setText(value);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setIncludeFontPadding(false);
        if (bold) view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return view;
    }

    private int dp(float value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private static class Poster {
        final String title;
        final String rating;
        final int backgroundRes;
        final int titleColor;
        final String artTitle;

        Poster(String title, String rating, int backgroundRes, int titleColor) {
            this(title, rating, backgroundRes, titleColor, title.toUpperCase());
        }

        Poster(String title, String rating, int backgroundRes, int titleColor, String artTitle) {
            this.title = title;
            this.rating = rating;
            this.backgroundRes = backgroundRes;
            this.titleColor = titleColor;
            this.artTitle = artTitle;
        }
    }
}
