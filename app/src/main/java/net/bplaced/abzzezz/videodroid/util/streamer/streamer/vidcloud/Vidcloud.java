package net.bplaced.abzzezz.videodroid.util.streamer.streamer.vidcloud;

import android.util.Log;
import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamer;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class Vidcloud extends Streamer {

    //TODO: Forge API (https://github.com/JohnnyBlackwater/Zephyr-mod/blob/2e9472793b45287b1114221f5dd1674ce886bca1/addons/script.module.resolveurl/lib/resolveurl/plugins/vidcloud9.py)

    @Override
    public Optional<ParcelableWatchableURLConnection> resolveStreamURL(String referral, final Optional<Map<String, String>> headers) throws IOException {
        referral = referral.replace("streaming.php", "download").replace("load.php", "download");

        Log.d("Stream VIDCloud-Streamer URL", referral);

        final Document document = Jsoup.connect(referral)
                .userAgent(Constant.USER_AGENT)
                .get();

        final Elements downloadElements = document.select(".dowload").select("a");

        int max = 0;
        Element maxE = null;

        for (final Element downloadElement : downloadElements) {
            final String str = downloadElement.ownText().replaceAll("[^0-9]+", "");
            if (StringUtil.isBlank(str)) continue;

            final int i = Integer.parseInt(str);
            if (i >= max && downloadElement.hasAttr("href") && !StringUtil.isBlank(downloadElement.attr("href"))) {
                max = i;
                maxE = downloadElement;
            }
        }

        if (maxE == null) {
            for (final Element downloadElement : downloadElements) {
                final String stripped = downloadElement.ownText().substring(downloadElement.ownText().indexOf(" ")).trim();

                for (final Streamers value : Streamers.values()) {
                    if (stripped.equalsIgnoreCase(value.getAlt()) && !StringUtil.isBlank(downloadElement.attr("href"))) {
                        Optional<ParcelableWatchableURLConnection> watchableURLConnection;
                        try {
                            watchableURLConnection = value.getStreamer().resolveStreamURL(downloadElement.attr("href"), Optional.of(Collections.singletonMap("Referer", referral)));
                        } catch (IOException | JSONException e) {
                            continue;
                        }
                        if (!watchableURLConnection.isPresent())
                            continue;
                        return watchableURLConnection;
                    }
                }
            }
            return Optional.empty();
        } else
            return Optional.of(new ParcelableWatchableURLConnection(maxE.attr("href"), 0, 0));
    }
}

