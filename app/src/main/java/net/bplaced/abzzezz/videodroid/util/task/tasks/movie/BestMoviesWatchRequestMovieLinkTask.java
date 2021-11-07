package net.bplaced.abzzezz.videodroid.util.task.tasks.movie;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.bestmovieswatch.BestMoviesWatchAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Optional;
import java.util.concurrent.Callable;

public class BestMoviesWatchRequestMovieLinkTask extends TaskExecutor implements Callable<Optional<ParcelableWatchableURLConnection>>, BestMoviesWatchAPI {

    public final Movie mMovie;

    public BestMoviesWatchRequestMovieLinkTask(final Movie mMovie) {
        this.mMovie = mMovie;
    }

    public void executeAsync(final Callback<Optional<ParcelableWatchableURLConnection>> callback) {
        super.executeAsync(this, callback);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        final String initialURL = this.formatMovieRequest(mMovie.getTitle(), mMovie.getId());

        final Document mainDocument = Jsoup.connect(initialURL)
                .timeout(0)
                .method(Connection.Method.GET)
                .followRedirects(false)
                .userAgent(Constant.USER_AGENT)
                .get();

        final Element iFrame = mainDocument.getElementById("frame");
        if (iFrame == null) return Optional.empty();

        return Streamers.VID_CLOUD.getStreamer().resolveStreamURL("https:" + iFrame.attr("src").replace("streaming.php", "download"));
    }
}
