package net.bplaced.abzzezz.videodroid.util.streamer.streamer.vidcloud;

import android.util.Log;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.connection.RandomUserAgent;
import net.bplaced.abzzezz.videodroid.util.crypto.AES;
import net.bplaced.abzzezz.videodroid.util.intermediate.GooglVideo;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.*;

public class Vidcloud extends Streamer implements VidCloudCCAPI {

    @Override
    public Optional<ParcelableWatchableURLConnection> resolveStreamURL(String referral, final Optional<Map<String, String>> headers) throws IOException {
        //referral = referral.replace("streaming.php", "download").replace("load.php", "download");
        if (!referral.startsWith("https://vidembed")) {
            referral = GooglVideo.resolveURL(referral, headers).orElse("");
        }
        if (referral.isEmpty())
            return Optional.empty();

        Log.d("Stream VIDCloud-Streamer URL", referral);

        final Map<String, List<String>> params = net.bplaced.abzzezz.videodroid.util.string.StringUtil.getQueryParams(referral);

        final String id = Objects.requireNonNull(params.get("id")).get(0);
        final String iv = rand(16);
        final String encrypted = AES.encrypt(id, AES_KEY, iv);

        final String url = formatAPIURL(encrypted, rand(2) + iv + rand(2));

        final Connection.Response response = Jsoup.connect(url)
                .method(Connection.Method.GET)
                .userAgent(RandomUserAgent.getRandomUserAgent())
                .referrer(BASE_URL)
                .header("X-Requested-With", "XMLHttpRequest")
                .execute();

        if (response.statusCode() != 200) {
            return Optional.empty();
        }

        try {
            final JSONObject jsonObject = new JSONObject(response.body());
            if (jsonObject.has("source")) {
                final JSONArray sourcesArray = jsonObject.getJSONArray("source");
                return getFromSource(sourcesArray, referral);
            } else if (jsonObject.has("source_bk")) {
                final JSONArray sourcesArray = jsonObject.getJSONArray("source_bk");
                return getFromSource(sourcesArray, referral);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Optional.empty();


        /*
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

         */
    }

    public Optional<ParcelableWatchableURLConnection> getFromSource(final JSONArray sourcesArray, final String referral) {
        for (int i = 0; i < sourcesArray.length(); i++) {
            final JSONObject object = sourcesArray.optJSONObject(i);
            if (object == null)
                continue;

            String file = object.optString("file");
            if (org.jsoup.internal.StringUtil.isBlank(file))
                continue;

            return Optional.of(new ParcelableWatchableURLConnection(file, Collections.singletonMap("Referer", referral)));
        }
        return Optional.empty();
    }

}

