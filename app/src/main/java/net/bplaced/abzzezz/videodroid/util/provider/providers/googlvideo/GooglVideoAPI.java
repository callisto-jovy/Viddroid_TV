package net.bplaced.abzzezz.videodroid.util.provider.providers.googlvideo;

import java.util.Locale;

public interface GooglVideoAPI {

    String BASE_URL = "https://googlvideo.com/";

    /*
    https://googlvideo.com/tmdb_api.php?se=1&ep=1&tmdb=60735&server_name=vcu
    https://googlvideo.com/tmdb_api?se=1&ep=1&tmdb=60735&server_name=vcs
    https://googlvideo.com/jadeed.php?ep=60735-6x5&server_name=serverf4&t=60735
     */

    String TV_ENDPOINT = BASE_URL + "/tmdb_api.php?se=%d&ep=%d&tmdb=%d&server_name=vcu";
    String MOVIE_ENDPOINT = BASE_URL + "/tmdb_api.php?&tmdb=%d&server_name=vcu";


    default String formatTVRequest(final int tvId, final int season, final int episode) {
        return String.format(Locale.ENGLISH, TV_ENDPOINT, season, episode, tvId);
    }

}
