package net.bplaced.abzzezz.videodroid.util.streamer.streamer.doodstream;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamer;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DoodStream extends Streamer {

    @Override
    public Optional<ParcelableWatchableURLConnection> resolveStreamURL(final String referral, final Optional<Map<String, String>> headers) throws IOException {
        final Document initialDocument = Jsoup.connect(referral)
                .headers(headers.orElseGet(HashMap::new))
                .userAgent(Constant.USER_AGENT)
                .get();

        final Element downloadElement = initialDocument.select(".download-content > a").first();

        if (downloadElement == null)
            return Optional.empty();

        final String downloadURL = downloadElement.attr("href");

        if (StringUtil.isBlank(downloadURL))
            return Optional.empty();

        final Document downloadPageDocument = Jsoup.connect("https://dood.la" + downloadURL)
                .referrer(referral)
                .userAgent(Constant.USER_AGENT)
                .get();

        final Element buttonElement = downloadPageDocument.getElementsByClass("btn btn-primary d-flex align-items-center justify-content-between")
                .first();

        if (buttonElement == null)
            return Optional.empty();

        final String onClickValue = buttonElement.attr("onclick");

        final int index = onClickValue.indexOf("window.open('");
        final String substring = onClickValue.substring(index + "window.open('".length(), onClickValue.indexOf("'", index));

        return Optional.of(new ParcelableWatchableURLConnection(substring, Collections.singletonMap("Referer", downloadURL)));
    }
}
