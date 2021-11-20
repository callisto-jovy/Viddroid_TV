package net.bplaced.abzzezz.videodroid.util.task.tasks.tv;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.googlvideo.GooglVideoAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;

import java.util.Collections;
import java.util.Optional;

public class GooglVideoRequestTVLinkTask extends TVLinkTask implements GooglVideoAPI {

    public GooglVideoRequestTVLinkTask(TVShow tvShow, int season, int episode) {
        super(tvShow, season, episode);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        final String url = formatTVRequest(getTvShow().getId(), getSeason(), getEpisode());
        return Streamers.VID_CLOUD.getStreamer().resolveStreamURL(url, Optional.of(Collections.singletonMap("Referrer", BASE_URL)));
    }
}
