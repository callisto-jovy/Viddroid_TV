package net.bplaced.abzzezz.videodroid.util.task.tasks.tv;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.array.ArrayUtil;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.connection.URLUtil;
import net.bplaced.abzzezz.videodroid.util.provider.providers.videovak.VideovakComAPI;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import org.jsoup.internal.StringUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class VideovakComRequestTVLinkTask extends TVLinkTask implements VideovakComAPI {

    public VideovakComRequestTVLinkTask(TVShow tvShow, int season, int episode) {
        super(tvShow, season, episode);
    }

    @Override
    public Optional<ParcelableWatchableURLConnection> call() throws Exception {
        //TODO: JSOUP refactor
        final HttpsURLConnection httpsURLConnection = URLUtil.createHTTPSURLConnection(STREAM_API_RESOLVER_ENDPOINT,
                new String[]{"Content-type", "application/x-www-form-urlencoded"},
                new String[]{"User-Agent", Constant.USER_AGENT});

        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.setDoOutput(true);
        httpsURLConnection.connect();

        try (final OutputStream os = httpsURLConnection.getOutputStream()) {
            os.write(("temp_request=" + formatStreamTemp(getTvShow().getTitle(), getSeason(), getEpisode())).getBytes(StandardCharsets.UTF_8));
        }

        String resp = URLUtil.collectLines(httpsURLConnection);

        if (StringUtil.isBlank(resp)) return Optional.empty();

        final String[] data = resp.split("-");
        if (data.length == 0) return Optional.empty();

        final String embed = STREAM_API_STREAM_FORMAT_ENDPOINT + data[1];

        return Optional.of(new ParcelableWatchableURLConnection
                (
                        embed,
                        ArrayUtil.stringArrayToMap(
                                new String[]{"Referrer", BASE_URL},
                                new String[]{"Range", "bytes=0-"}))
        );
    }
}
