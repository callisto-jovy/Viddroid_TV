package net.bplaced.abzzezz.videodroid.util.task.tasks.movie;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.movieshub.MoviesHubTechAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.Optional;

public class MoviesHubTechRequestMovieLinkTask extends MovieLinkTask implements MoviesHubTechAPI {

    public MoviesHubTechRequestMovieLinkTask(Movie movie) {
        super(movie);
    }

    public void executeAsync(final Callback<Optional<ParcelableWatchableURLConnection>> callback) {
        super.executeAsync(this, callback);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        final Document baseDocument = Jsoup.connect(formatMovieURL(getMovie().getId()))
                .userAgent(Constant.USER_AGENT)
                .get();

        final Element iFrame = baseDocument.getElementsByTag("iframe").first();
        if (iFrame == null) return Optional.empty();
        final String iFrameSrc = iFrame.attr("src");

        return Streamers.API_MDB.getStreamer().resolveStreamURL(iFrameSrc, Optional.of(Collections.singletonMap("Referrer", REFERRER)));
    }
}
