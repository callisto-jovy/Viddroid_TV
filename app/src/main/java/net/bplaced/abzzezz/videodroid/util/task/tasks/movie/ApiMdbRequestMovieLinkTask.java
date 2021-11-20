package net.bplaced.abzzezz.videodroid.util.task.tasks.movie;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.apimdb.ApiMdbAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;

import java.util.Collections;
import java.util.Optional;

public class ApiMdbRequestMovieLinkTask extends MovieLinkTask implements ApiMdbAPI {

    public ApiMdbRequestMovieLinkTask(Movie movie) {
        super(movie);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        final String url = formatMovieRequest(getMovie().getId());
        return Streamers.API_MDB.getStreamer().resolveStreamURL(url, Optional.of(Collections.singletonMap("Referrer", url)));
    }
}
