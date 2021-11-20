package net.bplaced.abzzezz.videodroid.util.provider.providers.apimdb;

import java.util.Locale;

public interface ApiMdbAPI {

    String BASE_URL = "https://v2.apimdb.net";

    String MOVIE_ENDPOINT = BASE_URL + "/e/tmdb/movie/%d";
    String SEASON_ENDPOINT = BASE_URL + "/e/tmdb/tv/%d/%d/%d";


    default String formatMovieRequest(final int movieId) {
        return String.format(Locale.ENGLISH, MOVIE_ENDPOINT, movieId);
    }

    default String formatTvRequest(final int tvId, int season, int episode) {
        return String.format(Locale.ENGLISH, SEASON_ENDPOINT, tvId, season, episode);
    }

}
