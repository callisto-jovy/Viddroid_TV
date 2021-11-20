package net.bplaced.abzzezz.videodroid.util.task.tasks.tv;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;

import java.util.Optional;
import java.util.concurrent.Callable;

public abstract class TVLinkTask extends TaskExecutor implements Callable<Optional<ParcelableWatchableURLConnection>> {

    private final TVShow tvShow;
    private final int episode, season;

    public TVLinkTask(TVShow tvShow, int season, int episode) {
        this.tvShow = tvShow;
        this.episode = episode;
        this.season = season;
    }

    public void executeAsync(final Callback<Optional<ParcelableWatchableURLConnection>> callback) {
        super.executeAsync(this, callback);
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public TVShow getTvShow() {
        return tvShow;
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        return Optional.empty();
    }
}
