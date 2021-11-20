package net.bplaced.abzzezz.videodroid.util.provider.providers.vidcloud;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.Provider;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import net.bplaced.abzzezz.videodroid.util.watchable.models.Episode;

import java.util.Optional;
import java.util.function.Consumer;

public class VidCloudCC extends Provider {
    @Override
    public void requestMovieLink(Movie watchable, Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer, Consumer<String> exceptionConsumer) {

    }

    @Override
    public void requestTVLink(TVShow watchable, Episode episode, Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer, Consumer<String> exceptionConsumer) {

    }
}
