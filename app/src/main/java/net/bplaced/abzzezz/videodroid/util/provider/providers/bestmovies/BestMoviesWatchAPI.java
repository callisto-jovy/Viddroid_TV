package net.bplaced.abzzezz.videodroid.util.provider.providers.bestmovies;

import net.bplaced.abzzezz.videodroid.util.string.StringUtil;

public interface BestMoviesWatchAPI {

    String BASE_URL = "https://best-movies.watch/movie/%s";

    default String formatMovieRequest(final String title, final int id) {
        return String.format(BASE_URL, id + "-" + StringUtil.dashes(title));
    }
}
