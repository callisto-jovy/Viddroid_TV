package net.bplaced.abzzezz.videodroid.util.intermediate;

import net.bplaced.abzzezz.videodroid.util.Constant;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GooglVideo {
    //https://googlvideo.com/

    public static Optional<String> resolveURL(String referral, Optional<Map<String, String>> headers) throws IOException {
        final Document document = Jsoup.connect(referral)
                .userAgent(Constant.USER_AGENT)
                .headers(headers.orElseGet(HashMap::new))
                .get();

        final String ref = document.getElementsByTag("iframe")
                .get(0)
                .attr("src");

        final Connection.Response response = Jsoup.connect(ref)
                .userAgent(Constant.USER_AGENT)
                .referrer(referral)
                .followRedirects(true)
                .execute();

        return Optional.of(response.url().toExternalForm());
    }
}
