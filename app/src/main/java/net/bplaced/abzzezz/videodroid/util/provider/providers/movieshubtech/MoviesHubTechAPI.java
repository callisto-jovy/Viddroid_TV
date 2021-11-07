package net.bplaced.abzzezz.videodroid.util.provider.providers.movieshubtech;

import net.bplaced.abzzezz.videodroid.util.watchable.Movie;

public interface MoviesHubTechAPI {

    String BASE_MOVIE_URL = "https://123movieshub.tech/movie/";
    String REFERRER = "https://123movieshub.tech/";

    default String formatMovieURL(final Movie movie) {
        return BASE_MOVIE_URL + movie.getId() + "/";
    }
}
