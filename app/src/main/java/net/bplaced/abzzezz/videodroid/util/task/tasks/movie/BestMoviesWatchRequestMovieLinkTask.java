package net.bplaced.abzzezz.videodroid.util.task.tasks.movie;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.bestmovies.BestMoviesWatchAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Optional;

public class BestMoviesWatchRequestMovieLinkTask extends MovieLinkTask implements BestMoviesWatchAPI {

    public BestMoviesWatchRequestMovieLinkTask(Movie movie) {
        super(movie);
    }

    public void executeAsync(final Callback<Optional<ParcelableWatchableURLConnection>> callback) {
        super.executeAsync(this, callback);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        final String initialURL = this.formatMovieRequest(getMovie().getTitle(), getMovie().getId());

        final Document mainDocument = Jsoup.connect(initialURL)
                .timeout(0)
                .method(Connection.Method.GET)
                .followRedirects(false)
                .userAgent(Constant.USER_AGENT)
                .get();

        final Element iFrame = mainDocument.getElementById("frame");
        if (iFrame == null) return Optional.empty();

        return Streamers.VID_CLOUD.getStreamer().resolveStreamURL("https:" + iFrame.attr("src").replace("streaming.php", "download"), Optional.empty());
    }
}
