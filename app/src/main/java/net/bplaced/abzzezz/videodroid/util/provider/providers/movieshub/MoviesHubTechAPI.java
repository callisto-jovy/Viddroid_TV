package net.bplaced.abzzezz.videodroid.util.provider.providers.movieshub;

public interface MoviesHubTechAPI {

    String BASE_MOVIE_URL = "https://123movieshub.tech/movie/";
    String REFERRER = "https://123movieshub.tech/";

    default String formatMovieURL(final int movieId) {
        return BASE_MOVIE_URL + movieId + "/";
    }
}
