package net.bplaced.abzzezz.videodroid.util.provider.providers.bestmovies;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.Provider;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.task.tasks.movie.BestMoviesWatchRequestMovieLinkTask;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import net.bplaced.abzzezz.videodroid.util.watchable.models.Episode;

import java.util.Optional;
import java.util.function.Consumer;

public class BestMoviesWatch extends Provider {


    @Override
    public void requestMovieLink(Movie watchable, Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer, Consumer<String> exceptionConsumer) {
        new BestMoviesWatchRequestMovieLinkTask(watchable).executeAsync(new TaskExecutor.Callback<Optional<ParcelableWatchableURLConnection>>() {
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
        directConnectionConsumer.accept(Optional.empty()); //Bestmovies.watch does not have tv-shows
    }
}
