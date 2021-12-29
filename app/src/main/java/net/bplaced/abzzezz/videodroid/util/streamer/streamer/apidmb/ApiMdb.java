package net.bplaced.abzzezz.videodroid.util.streamer.streamer.apidmb;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.apimdb.ApiMdbAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamer;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamers;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class ApiMdb extends Streamer implements ApiMdbAPI {
    @Override
    public Optional<ParcelableWatchableURLConnection> resolveStreamURL(String referral, Optional<Map<String, String>> headers) throws IOException, JSONException {
        if (StringUtil.isBlank(referral))
            return Optional.empty();

        final String referrer = headers.map(stringStringMap -> stringStringMap.get("Referrer")).orElse("");

        final Document apiDocument = Jsoup.connect(referral)
                .userAgent(Constant.USER_AGENT)
                .referrer(referrer)
                .get();

        final Elements streamList = apiDocument.select(".server");

        for (final Element element : streamList) {
            final String attr = element.attr("data-src");
            final String text = element.text();
            if (!StringUtil.isBlank(attr) && !StringUtil.isBlank(text)) {
                for (final Streamers value : Streamers.values()) {
                    if (value.getAlt().contains(text)) {
                        final Document document = Jsoup
                                .connect(BASE_URL)
                                .followRedirects(true)
                                .referrer(referrer)
                                .userAgent(Constant.USER_AGENT)
                                .get();

                        final Element selectedElement = document.getElementById("frame");
                        if (selectedElement == null)
                            continue;

                        if (!selectedElement.hasAttr("src"))
                            continue;

                        final Optional<ParcelableWatchableURLConnection> parcelableMovieURLConnectionOptional = value.getStreamer().resolveStreamURL(selectedElement.attr("src"), Optional.empty());

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
