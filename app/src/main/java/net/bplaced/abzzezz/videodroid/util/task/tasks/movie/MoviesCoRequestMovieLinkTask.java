package net.bplaced.abzzezz.videodroid.util.task.tasks.movie;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.moviesco.MoviesCoAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Optional;

public class MoviesCoRequestMovieLinkTask extends MovieLinkTask implements MoviesCoAPI {

    public MoviesCoRequestMovieLinkTask(Movie movie) {
        super(movie);
    }

    public void executeAsync(final Callback<Optional<ParcelableWatchableURLConnection>> callback) {
        super.executeAsync(this, callback);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        final String url = formatMovieRequest(getMovie().getTitle());
        final Document document = Jsoup.connect(url)
                .userAgent(Constant.USER_AGENT)
                .get();

        final Element iFrame = document.getElementsByTag("iframe").first();

        if (iFrame == null) return Optional.empty();

        final String embed = iFrame.attr("src");

        return Streamers.GOMO.getStreamer().resolveStreamURL(embed, Optional.empty());
    }
}
