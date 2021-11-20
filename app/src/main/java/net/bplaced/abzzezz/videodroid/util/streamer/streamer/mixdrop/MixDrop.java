package net.bplaced.abzzezz.videodroid.util.streamer.streamer.mixdrop;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamer;
import net.bplaced.abzzezz.videodroid.util.string.JsUnpacker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MixDrop extends Streamer {

    public static final Pattern MIXDROP_WURL_PATTERN = Pattern.compile("(?<=MDCore.wurl=\")[^\"]+");

    @Override
    public Optional<ParcelableWatchableURLConnection> resolveStreamURL(String referral, final Optional<Map<String, String>> headers) throws IOException {
        referral = referral.replace("/f/", "/e/");

        final Document document = Jsoup.connect(referral).get();

        final Element scriptElement = document.getElementsByTag("script")
                .stream()
                .filter(element -> element.data().contains("eval"))
                .findAny()
                .orElse(null);

        if (scriptElement == null) return Optional.empty();

        final JsUnpacker packer = new JsUnpacker(scriptElement.data());
        final String unpacked = packer.unpack();

        final Matcher wurlPatternMatcher = MIXDROP_WURL_PATTERN.matcher(unpacked);
        if (wurlPatternMatcher.find()) {
            final String directURL = "https://" + wurlPatternMatcher.group().replaceAll("&_t=(.*)+", "");

            return Optional.of(new ParcelableWatchableURLConnection(directURL, Collections.singletonMap("Referer", "https://mixdrop.co/")));
        }
        return Optional.empty();
    }
}
