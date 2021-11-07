package net.bplaced.abzzezz.videodroid.util.provider.providers.movieshubtech;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.Provider;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.task.tasks.movie.MoviesHubTechRequestMovieLinkTask;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;

import java.util.Optional;
import java.util.function.Consumer;

public class MoviesHubTech extends Provider {

    @Override
    public void requestMovieLink(final Movie movie, final Consumer<Optional<ParcelableWatchableURLConnection>> directLink) {
        new MoviesHubTechRequestMovieLinkTask(movie).executeAsync(new TaskExecutor.Callback<Optional<ParcelableWatchableURLConnection>>() {
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
        directConnectionConsumer.accept(Optional.empty()); //123movieshub.tech does not have tv-shows
    }


}
