
package com.example.cinediary.data.remote.tmdb;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TmdbClient {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    // Cole aqui seu Read Access Token
    private static final String READ_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Y2U0MzVmYjI5YWFjNTAwY2YxMTVhN2I1ZmRiOTUyMCIsIm5iZiI6MTc5MzYyMTI5MS44MjksInN1YiI6IjZhZTg3ZDJiNTMxMzlmODMyYmE2ZWFiZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.D6PF0D64jdC6h0qH-zAEwmE70bXldLBDSPhv3AtGndw";

    private static TmdbApi api;

    public static TmdbApi getApi() {

        if (api == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    )
                    .build();

            api = retrofit.create(TmdbApi.class);
        }

        return api;
    }

    public static String getAuthorizationHeader() {
        return "Bearer " + READ_ACCESS_TOKEN;
    }
}
