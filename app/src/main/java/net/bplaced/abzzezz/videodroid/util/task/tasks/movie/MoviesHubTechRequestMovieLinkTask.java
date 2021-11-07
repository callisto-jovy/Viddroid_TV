package net.bplaced.abzzezz.videodroid.util.task.tasks.movie;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.movieshubtech.MoviesHubTechAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Optional;
import java.util.concurrent.Callable;

public class MoviesHubTechRequestMovieLinkTask extends TaskExecutor implements Callable<Optional<ParcelableWatchableURLConnection>>, MoviesHubTechAPI {

    private final Movie mMovie;

    public MoviesHubTechRequestMovieLinkTask(final Movie mMovie) {
        this.mMovie = mMovie;
    }


    public void executeAsync(final Callback<Optional<ParcelableWatchableURLConnection>> callback) {
        super.executeAsync(this, callback);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        final Document baseDocument = Jsoup.connect(formatMovieURL(mMovie))
                .userAgent(Constant.USER_AGENT)
                .get();

        final Element iFrame = baseDocument.getElementsByTag("iframe").first();
        if (iFrame == null) return Optional.empty();
        final String iFrameSrc = iFrame.attr("src");

        final Document apiDocument = Jsoup.connect(iFrameSrc)
                .userAgent(Constant.USER_AGENT)
                .referrer(REFERRER)
                .get();

        final Elements streamList = apiDocument.select(".server");

        for (final Element element : streamList) {
            final String attr = element.attr("data-src");
            final String text = element.text();
            if (!StringUtil.isBlank(attr) && !StringUtil.isBlank(text)) {
                for (final Streamers value : Streamers.values()) {
                    if (value.getAlt().equalsIgnoreCase(text)) {

                        final Document document = Jsoup
                                .connect("https://v2.apimdb.net/" + attr)
                                .followRedirects(true)
                                .referrer("https://123movieshub.tech/")
                                .userAgent(Constant.USER_AGENT)
                                .get();

                        final Element selectedElement = document.getElementById("frame");
                        if (!selectedElement.hasAttr("src")) continue;

                        final Optional<ParcelableWatchableURLConnection> parcelableMovieURLConnectionOptional = value.getStreamer().resolveStreamURL(selectedElement.attr("src"));

                        if (!parcelableMovieURLConnectionOptional.isPresent())
                            continue;

                        return parcelableMovieURLConnectionOptional;

                    }
                }
            }
        }
        return Optional.empty();
    }
}
