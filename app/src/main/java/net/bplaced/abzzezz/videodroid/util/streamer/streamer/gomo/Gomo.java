package net.bplaced.abzzezz.videodroid.util.streamer.streamer.gomo;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.moviesco.MoviesCoAPI;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamer;
import net.bplaced.abzzezz.videodroid.util.string.JsUnpacker;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;

public class Gomo extends Streamer implements MoviesCoAPI {

    @Override
    public Optional<ParcelableWatchableURLConnection> resolveStreamURL(final String url, final String[]... headers) throws IOException, JSONException {
        if (StringUtil.isBlank(url)) return Optional.empty();

        final Document embedPlayer = Jsoup.connect(url)
                .userAgent(Constant.USER_AGENT)
                .get();

        final String embedPlayerContent = embedPlayer.toString();

        final Matcher tokenMatcher = TOKEN_PATTERN.matcher(embedPlayerContent);
        final Matcher tcMatcher = TC_PATTERN.matcher(embedPlayerContent);
        final Matcher functionMatcher = FUNCTION_PATTERN.matcher(embedPlayerContent);

        if (tokenMatcher.find() && tcMatcher.find() && functionMatcher.find()) {
            //String s = functionMatcher.group();

            final String tcGroup = tcMatcher.group(1);
            final String tokenGroup = tokenMatcher.group(1);


            final Matcher sliceMatcher = SLICE_PATTERN.matcher(embedPlayerContent);
            final Matcher rndNumberMatcher = RND_NUM_PATTERN.matcher(embedPlayerContent);

            if (sliceMatcher.find()) {
                String slideStart = sliceMatcher.group(1);
                String slideEnd = sliceMatcher.group(2);

                assert slideStart != null;
                assert slideEnd != null;
                assert tcGroup != null;
                String xToken = new StringBuilder(tcGroup.substring(Integer.parseInt(slideStart), Integer.parseInt(slideEnd)))
                        .reverse() + (
                        rndNumberMatcher.find()
                                ? rndNumberMatcher.group(1) + rndNumberMatcher.group(2)
                                : "");

                /*
                s = s.concat("_tsd_tsd_ds('" + tcGroup + "')");

                final V8 runtime = V8.createV8Runtime();
                String result = runtime.executeStringScript(s);
                runtime.release();
*/

                assert tokenGroup != null;
                final Connection.Response decodingAPIResp = Jsoup.connect(DECODING_API)
                        .method(Connection.Method.POST)
                        .data("tokenCode", tcGroup)
                        .data("_token", tokenGroup)
                        .header("x-token", xToken)
                        .referrer(url)
                        .userAgent(Constant.USER_AGENT)
                        .execute();

                if (StringUtil.isBlank(decodingAPIResp.body())) return Optional.empty();

                final JSONArray referralArray = new JSONArray(decodingAPIResp.body());

                for (int i = 0; i < referralArray.length(); i++) {
                    final String referral = referralArray.getString(i);
                    //TODO: Referral to different streamers

                    if (referral.startsWith("https://gomo")) {
                        final Document videoDocument = Jsoup.connect(referral)
                                .referrer(url)
                                .userAgent(Constant.USER_AGENT)
                                .get();

                        final Optional<Element> element = videoDocument.getElementsByTag("script")
                                .stream()
                                .filter(element1 -> element1.data().startsWith("eval"))
                                .findAny();

                        if (element.isPresent()) {
                            final JsUnpacker unpacker = new JsUnpacker(element.get().data());
                            final Matcher sourceMatcher = SOURCE_PATTERN.matcher(unpacker.unpack());

                            if (sourceMatcher.find()) {
                                return Optional.of(new ParcelableWatchableURLConnection(sourceMatcher.group(), new String[]{"Referer", referral}));
                            }
                        }

                    }
                }
            }
        }
        return Optional.empty();
    }

}
