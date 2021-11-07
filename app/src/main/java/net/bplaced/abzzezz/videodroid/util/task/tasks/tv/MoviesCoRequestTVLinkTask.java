package net.bplaced.abzzezz.videodroid.util.task.tasks.tv;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.moviesco.MoviesCoAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Optional;
import java.util.concurrent.Callable;

public class MoviesCoRequestTVLinkTask extends TaskExecutor implements Callable<Optional<ParcelableWatchableURLConnection>>, MoviesCoAPI {

    private final TVShow mTVShow;
    private final int season, episode;

    public MoviesCoRequestTVLinkTask(final TVShow mTVShow, int season1, int episode1) {
        this.mTVShow = mTVShow;
        this.season = season1;
        this.episode = episode1;
    }

    public void executeAsync(final Callback<Optional<ParcelableWatchableURLConnection>> callback) {
        super.executeAsync(this, callback);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        final String url = formatTvRequest(mTVShow, season, episode);
        final Document document = Jsoup.connect(url)
                .userAgent(Constant.USER_AGENT)
                .get();

        final Element iFrame = document.selectFirst(".playerLock > iframe");

        if (iFrame == null) return Optional.empty();

        final String embed = iFrame.attr("src");

        return Streamers.GOMO.getStreamer().resolveStreamURL(embed);
    }
}