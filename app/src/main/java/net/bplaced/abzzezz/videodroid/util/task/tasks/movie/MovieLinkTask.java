package net.bplaced.abzzezz.videodroid.util.task.tasks.movie;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;

import java.util.Optional;
import java.util.concurrent.Callable;

public abstract class MovieLinkTask extends TaskExecutor implements Callable<Optional<ParcelableWatchableURLConnection>> {

    private final Movie movie;

    public MovieLinkTask(Movie movie) {
        this.movie = movie;
    }

    public void executeAsync(final Callback<Optional<ParcelableWatchableURLConnection>> callback) {
        super.executeAsync(this, callback);
    }

    public Movie getMovie() {
        return movie;
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        return Optional.empty();
    }
}
