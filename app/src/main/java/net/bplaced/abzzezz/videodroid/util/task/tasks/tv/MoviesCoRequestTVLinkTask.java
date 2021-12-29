package net.bplaced.abzzezz.videodroid.util.task.tasks.tv;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.moviesco.MoviesCoAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Optional;

public class MoviesCoRequestTVLinkTask extends TVLinkTask implements MoviesCoAPI {


    public MoviesCoRequestTVLinkTask(TVShow tvShow, int season, int episode) {
        super(tvShow, season, episode);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        final String url = formatTvRequest(getTvShow(), getSeason(), getEpisode());

        final Document document = Jsoup.connect(url)
                .userAgent(Constant.USER_AGENT)
                .get();

        final Element iFrame = document.selectFirst(".playerLock > iframe");

        if (iFrame == null) return Optional.empty();

        final String embed = iFrame.attr("src");

        return Streamers.GOMO.getStreamer().resolveStreamURL(embed, Optional.empty());
    }
}