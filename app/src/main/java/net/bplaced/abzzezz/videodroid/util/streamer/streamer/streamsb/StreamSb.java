package net.bplaced.abzzezz.videodroid.util.streamer.streamer.streamsb;

import android.util.Log;
import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.array.ArrayUtil;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.moviesco.MoviesCoAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamer;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

public class StreamSb extends Streamer {

    @Override
    public Optional<ParcelableWatchableURLConnection> resolveStreamURL(String url, final String[]... headers) throws JSONException {
        url = url.replace("/play/", "/d/");
        Log.d("Stream SB-Streamer URL", url);

        final Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent(Constant.USER_AGENT)
                    .headers(ArrayUtil.stringArrayToMap(headers))
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        final Matcher onClickMatcher = MoviesCoAPI.ONCLICK_PATTERN.matcher(document.toString());

        if (onClickMatcher.find()) {
            final String id = onClickMatcher.group(1);
            final String mode = onClickMatcher.group(2);
            final String hash = onClickMatcher.group(3);

            String format = String.format("https://sbplay1.com/dl?op=download_orig&id=%s&mode=%s&hash=%s", id, mode, hash);

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Document content = Jsoup.connect(format)
                        .userAgent(Constant.USER_AGENT)
                        .header("Referer", url)
                        .get();

                final Matcher directDownloadLinkMatcher = MoviesCoAPI.DIRECT_DOWNLOAD_LINK_PATTERN.matcher(content.toString());

                if (directDownloadLinkMatcher.find()) {
                    return Optional.of(new ParcelableWatchableURLConnection(
                            directDownloadLinkMatcher.group(1),
                            new String[]{"Referer", format}));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
