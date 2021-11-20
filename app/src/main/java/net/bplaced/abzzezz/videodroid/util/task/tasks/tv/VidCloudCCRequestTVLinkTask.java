package net.bplaced.abzzezz.videodroid.util.task.tasks.tv;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;

import java.util.Optional;

public class VidCloudCCRequestTVLinkTask extends TVLinkTask {

    public VidCloudCCRequestTVLinkTask(TVShow tvShow, int season, int episode) {
        super(tvShow, season, episode);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {


        return Optional.empty();
    }
}
