package net.bplaced.abzzezz.videodroid.util.provider.providers.bestmovieswatch;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.Provider;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.task.tasks.movie.BestMoviesWatchRequestMovieLinkTask;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;

import java.util.Optional;
import java.util.function.Consumer;

public class BestMoviesWatch extends Provider {

    @Override
    public void requestMovieLink(final Movie movie, final Consumer<Optional<ParcelableWatchableURLConnection>> directLink) {
        new BestMoviesWatchRequestMovieLinkTask(movie).executeAsync(new TaskExecutor.Callback<Optional<ParcelableWatchableURLConnection>>() {
            @Override
            public void onComplete(final Optional<ParcelableWatchableURLConnection> result) {
                directLink.accept(result);
            }

            @Override
            public void preExecute() {

            }

            @Override
            public void exceptionCaught(Exception e) {
                directLink.accept(Optional.empty());
            }
        });
    }

    @Override
    public void requestTVLink(final TVShow tvShow, final int season, final int episode, final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer) {
        directConnectionConsumer.accept(Optional.empty()); //Bestmovies.watch does not have tv-shows
    }
}
