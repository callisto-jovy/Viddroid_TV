package net.bplaced.abzzezz.videodroid.util.provider.providers.videovakcom;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.Provider;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.task.tasks.tv.VideovakComRequestTVLinkTask;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;

import java.util.Optional;
import java.util.function.Consumer;

public class VideovakCom extends Provider {

    @Override
    public void requestMovieLink(Movie watchable, Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer) {
        directConnectionConsumer.accept(Optional.empty());
    }

    @Override
    public void requestTVLink(TVShow watchable, int season, int episode, Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer) {
        new VideovakComRequestTVLinkTask(watchable, season, episode).executeAsync(new TaskExecutor.Callback<Optional<ParcelableWatchableURLConnection>>() {
            @Override
            public void onComplete(Optional<ParcelableWatchableURLConnection> result) {
                directConnectionConsumer.accept(result);
            }

            @Override
            public void preExecute() {

            }

            @Override
            public void exceptionCaught(Exception e) {
                directConnectionConsumer.accept(Optional.empty());
            }
        });
    }
}
