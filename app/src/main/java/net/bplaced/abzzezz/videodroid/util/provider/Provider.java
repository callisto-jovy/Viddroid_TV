package net.bplaced.abzzezz.videodroid.util.provider;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class Provider {

    public abstract void requestMovieLink(final Movie watchable, final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer);

    public abstract void requestTVLink(final TVShow watchable, final int season, final int episode, final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer);
}
