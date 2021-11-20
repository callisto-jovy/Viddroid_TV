package net.bplaced.abzzezz.videodroid.util.provider.providers.movieshub;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.Provider;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.task.tasks.movie.MoviesHubTechRequestMovieLinkTask;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import net.bplaced.abzzezz.videodroid.util.watchable.models.Episode;

import java.util.Optional;
import java.util.function.Consumer;

public class MoviesHubTech extends Provider {

    @Override
    public void requestMovieLink(Movie watchable, Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer, Consumer<String> exceptionConsumer) {
        new MoviesHubTechRequestMovieLinkTask(watchable).executeAsync(new TaskExecutor.Callback<Optional<ParcelableWatchableURLConnection>>() {
            @Override
            public void onComplete(final Optional<ParcelableWatchableURLConnection> result) {
                directConnectionConsumer.accept(result);
            }

            @Override
            public void preExecute() {

            }

            @Override
            public void exceptionCaught(Exception e) {
                exceptionConsumer.accept(e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void requestTVLink(TVShow watchable, Episode episode, Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer, Consumer<String> exceptionConsumer) {
        directConnectionConsumer.accept(Optional.empty()); //123movieshub.tech does not have tv-shows

    }
}
