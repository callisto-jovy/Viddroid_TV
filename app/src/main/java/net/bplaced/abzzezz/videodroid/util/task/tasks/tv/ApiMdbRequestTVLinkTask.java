package net.bplaced.abzzezz.videodroid.util.task.tasks.tv;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.apimdb.ApiMdbAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;

import java.util.Collections;
import java.util.Optional;

public class ApiMdbRequestTVLinkTask extends TVLinkTask implements ApiMdbAPI {

    public ApiMdbRequestTVLinkTask(TVShow tvShow, int season, int episode) {
        super(tvShow, season, episode);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        final String url = formatTvRequest(getTvShow().getId(), getSeason(), getEpisode());
        return Streamers.API_MDB.getStreamer().resolveStreamURL(url, Optional.of(Collections.singletonMap("Referrer", url)));
    }
}
