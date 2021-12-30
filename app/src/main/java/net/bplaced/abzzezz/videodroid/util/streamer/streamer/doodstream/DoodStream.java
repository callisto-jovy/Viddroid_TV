package net.bplaced.abzzezz.videodroid.util.streamer.streamer.doodstream;

import android.util.Log;
import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;

public class DoodStream extends Streamer implements DoodStreamAPI {

    @Override
    public Optional<ParcelableWatchableURLConnection> resolveStreamURL(final String referral, final Optional<Map<String, String>> headers) throws IOException {
        if (StringUtil.isBlank(referral))
            return Optional.empty();

        final Document initialDocument = Jsoup.connect(referral)
                .headers(headers.orElseGet(HashMap::new))
                .userAgent(Constant.USER_AGENT)
                .get();

        if (StringUtil.isBlank(initialDocument.toString())) {
            return Optional.empty();
        }

        final Matcher matcher = PASS_MD_5_PATTERN.matcher(initialDocument.toString());
        if (matcher.find()) {
            final String passMD5 = matcher.group();
            final String token = passMD5.substring(passMD5.lastIndexOf('/') + 1);
            System.out.println("token = " + token);


            final String passMD5URL = BASE_URL + passMD5;

            final Connection.Response response = Jsoup.connect(passMD5URL)
                    .method(Connection.Method.GET)
                    .userAgent(Constant.USER_AGENT)
                    .header("X-Requested-With", "XMLHttpRequest")
                    .referrer(referral)
                    .execute();

            if (StringUtil.isBlank(response.body()))
                return Optional.empty();


            final String responseBody = response.body() + generateSuffix(token);


            Log.d("DoodStream", responseBody);
            final Map<String, String> map = new HashMap<>();
            map.put("Referrer", BASE_URL);
            map.put("Range", "bytes=0-");

            return Optional.of(new ParcelableWatchableURLConnection(responseBody, map));
        } else
            return Optional.empty();


        /*
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

         */
    }
}
