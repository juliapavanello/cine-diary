package com.example.cinediary.data.remote.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Resposta de movie/{id} ou tv/{id} com append_to_response=credits.
 * Filmes usam title/release_date/runtime; séries usam name/first_air_date/episode_run_time.
 */
public class TmdbDetalhes {

    private int id;
    private String title;
    private String name;
    private String overview;
    private String tagline;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("first_air_date")
    private String firstAirDate;

    private Integer runtime;

    @SerializedName("episode_run_time")
    private List<Integer> episodeRunTime;

    @SerializedName("number_of_seasons")
    private Integer numberOfSeasons;

    @SerializedName("last_air_date")
    private String lastAirDate;

    @SerializedName("in_production")
    private Boolean inProduction;

    private double popularity;

    private List<Genero> genres;
    private Creditos credits;

    @SerializedName("watch/providers")
    private Provedores watchProviders;

    private Reviews reviews;

    // ---------- Getters "prontos para a tela" ----------

    public int getId() { return id; }

    public String getTitulo() {
        if (title != null) return title;
        return name != null ? name : "Sem título";
    }

    public String getSinopse() { return overview; }
    public String getTagline() { return tagline; }
    public String getPosterPath() { return posterPath; }
    public String getBackdropPath() { return backdropPath; }
    public double getNota() { return voteAverage; }
    public int getQuantidadeVotos() { return voteCount; }

    /** "2013", ou null se não tiver data. */
    public String getAno() {
        String data = releaseDate != null && !releaseDate.isEmpty() ? releaseDate : firstAirDate;
        return data != null && data.length() >= 4 ? data.substring(0, 4) : null;
    }

    /** Filme: "2013". Série: "2018 - Presente" ou "2016 - 2022". */
    public String getPeriodo() {
        String inicio = getAno();
        if (inicio == null || firstAirDate == null || firstAirDate.isEmpty()) return inicio;
        if (Boolean.TRUE.equals(inProduction)) return inicio + " - Presente";
        if (lastAirDate != null && lastAirDate.length() >= 4) {
            String fim = lastAirDate.substring(0, 4);
            return fim.equals(inicio) ? inicio : inicio + " - " + fim;
        }
        return inicio;
    }

    public double getPopularidade() { return popularity; }

    /** Streamings por assinatura no Brasil (pode vir vazio). */
    public List<Provedor> getProvedoresBrasil() {
        if (watchProviders == null || watchProviders.results == null) return new ArrayList<>();
        Pais br = watchProviders.results.get("BR");
        if (br == null || br.flatrate == null) return new ArrayList<>();
        return br.flatrate;
    }

    /** Até 5 reviews. */
    public List<Review> getReviews() {
        if (reviews == null || reviews.results == null) return new ArrayList<>();
        return reviews.results.size() > 5 ? reviews.results.subList(0, 5) : reviews.results;
    }

    /** Usado quando não há reviews em português e buscamos em inglês. */
    public void setReviews(List<Review> lista) {
        if (reviews == null) reviews = new Reviews();
        reviews.results = lista;
    }

    /** Filme: "2h 07min". Série: "3 temporadas". */
    public String getDuracao() {
        if (numberOfSeasons != null && numberOfSeasons > 0) {
            return numberOfSeasons == 1 ? "1 temporada" : numberOfSeasons + " temporadas";
        }
        Integer minutos = runtime;
        if ((minutos == null || minutos == 0) && episodeRunTime != null && !episodeRunTime.isEmpty()) {
            minutos = episodeRunTime.get(0);
        }
        if (minutos == null || minutos == 0) return null;
        int h = minutos / 60, m = minutos % 60;
        return h > 0 ? String.format(Locale.getDefault(), "%dh %02dmin", h, m) : m + "min";
    }

    public List<String> getNomesGeneros() {
        List<String> nomes = new ArrayList<>();
        if (genres != null) for (Genero g : genres) nomes.add(g.name);
        return nomes;
    }

    /** Até 15 nomes do elenco principal. */
    public List<Ator> getElenco() {
        if (credits == null || credits.cast == null) return new ArrayList<>();
        return credits.cast.size() > 15 ? credits.cast.subList(0, 15) : credits.cast;
    }

    // ---------- Classes internas do JSON ----------

    public static class Genero {
        private int id;
        private String name;
    }

    public static class Creditos {
        private List<Ator> cast;
    }

    public static class Provedores {
        private Map<String, Pais> results;
    }

    public static class Pais {
        private List<Provedor> flatrate;
    }

    public static class Provedor {
        @SerializedName("provider_name")
        private String nome;

        @SerializedName("logo_path")
        private String logoPath;

        public String getNome() { return nome; }
        public String getLogoPath() { return logoPath; }
    }

    public static class Reviews {
        private List<Review> results;
        public List<Review> getResults() { return results; }
    }

    public static class Review {
        private String author;
        private String content;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("author_details")
        private Autor autor;

        public String getAutor() { return author; }
        public String getTexto() { return content; }

        /** "2024-03-15T..." -> "15/03/2024" */
        public String getData() {
            if (createdAt == null || createdAt.length() < 10) return null;
            return createdAt.substring(8, 10) + "/" + createdAt.substring(5, 7) + "/" + createdAt.substring(0, 4);
        }

        /** Nota de 0 a 10 dada pelo autor, ou null. */
        public Double getNota() { return autor != null ? autor.rating : null; }

        public String getAvatarPath() { return autor != null ? autor.avatarPath : null; }
    }

    public static class Autor {
        private Double rating;

        @SerializedName("avatar_path")
        private String avatarPath;
    }

    public static class Ator {
        private String name;
        private String character;

        @SerializedName("profile_path")
        private String profilePath;

        public String getNome() { return name; }
        public String getPersonagem() { return character; }
        public String getFotoPath() { return profilePath; }
    }
}
