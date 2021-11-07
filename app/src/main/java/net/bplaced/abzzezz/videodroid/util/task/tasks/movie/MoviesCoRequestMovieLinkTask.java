package net.bplaced.abzzezz.videodroid.util.task.tasks.movie;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.moviesco.MoviesCoAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Optional;
import java.util.concurrent.Callable;

public class MoviesCoRequestMovieLinkTask extends TaskExecutor implements Callable<Optional<ParcelableWatchableURLConnection>>, MoviesCoAPI {

    private final Movie mMovie;

    public MoviesCoRequestMovieLinkTask(final Movie mMovie) {
        this.mMovie = mMovie;
    }

    public void executeAsync(final Callback<Optional<ParcelableWatchableURLConnection>> callback) {
        super.executeAsync(this, callback);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        final String url = formatMovieRequest(mMovie);
        final Document document = Jsoup.connect(url)
                .userAgent(Constant.USER_AGENT)
                .get();

        final Element iFrame = document.getElementsByTag("iframe").first();

        if (iFrame == null) return Optional.empty();

        final String embed = iFrame.attr("src");

        return Streamers.GOMO.getStreamer().resolveStreamURL(embed);
    }
}
