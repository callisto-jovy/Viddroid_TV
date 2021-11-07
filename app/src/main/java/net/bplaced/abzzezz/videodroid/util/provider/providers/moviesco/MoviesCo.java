package net.bplaced.abzzezz.videodroid.util.provider.providers.moviesco;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.Provider;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.task.tasks.movie.MoviesCoRequestMovieLinkTask;
import net.bplaced.abzzezz.videodroid.util.task.tasks.tv.MoviesCoRequestTVLinkTask;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;

import java.util.Optional;
import java.util.function.Consumer;

public class MoviesCo extends Provider {

    @Override
    public void requestMovieLink(final Movie movie, final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer) {
        new MoviesCoRequestMovieLinkTask(movie).executeAsync(new TaskExecutor.Callback<Optional<ParcelableWatchableURLConnection>>() {
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

    @Override
    public void requestTVLink(final TVShow movie, final int season, final int episode, final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer) {
        new MoviesCoRequestTVLinkTask(movie, season, episode).executeAsync(new TaskExecutor.Callback<Optional<ParcelableWatchableURLConnection>>() {
            @Override
            public void onComplete(final Optional<ParcelableWatchableURLConnection> result) {
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
