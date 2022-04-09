package net.bplaced.abzzezz.videodroid.util.streamer.streamer.doodstream;

import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.streamer.Streamer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoodStream extends Streamer implements DoodStreamAPI {

    @Override
    public Optional<ParcelableWatchableURLConnection> resolveStreamURL(String referral, final Optional<Map<String, String>> headers) throws IOException {
        if (StringUtil.isBlank(referral))
            return Optional.empty();


        System.out.println(referral);


        boolean isLong = false;

        if (referral.contains("LONG")) {
            isLong = true;
            referral = referral.replace("LONG", "");
        } else {
            referral = referral.replace("/d/", "/e/");
        }


        final String finalUrl = referral;
        String token = "";
        String urlt = "";

        Connection.Response resp = Jsoup.connect(referral)
                .method(Connection.Method.GET)
                .userAgent(Constant.USER_AGENT)
                .execute();

        String response = resp.body();

        if (isLong) {
            Pattern pattern = Pattern.compile("<iframe src=\"(.*?)\"");
            Matcher matcher = pattern.matcher(response);
            if (matcher.find()) {
                Connection.Response resp1 = Jsoup.connect("https://" + getHost(matcher.group(1)))
                        .method(Connection.Method.GET)
                        .userAgent(Constant.USER_AGENT)
                        .execute();

                String response1 = resp1.body();

                Pattern pattern2 = Pattern.compile("dsplayer\\.hotkeys[^']+'([^']+).+?function");
                Matcher matcher2 = pattern2.matcher(response1);
                if (matcher2.find()) {

                    urlt = "https://" + getHost(finalUrl) + matcher2.group(1);


                    pattern2 = Pattern.compile("makePlay.+?return[^?]+([^\"]+)");
                    matcher2 = pattern2.matcher(response1);
                    if (matcher2.find()) {
                        token = matcher2.group(1);

                        Connection.Response resp2 = Jsoup.connect("https://" + getHost(matcher.group(1)))
                                .method(Connection.Method.GET)
                                .userAgent(Constant.USER_AGENT)
                                .referrer(finalUrl)
                                .execute();

                        String response2 = resp2.body();

                        String test = response2 + randomStr(10) + token + System.currentTimeMillis() / 1000L;


                        System.out.println(test);

                    }
                }
            }
        } else {
            Pattern pattern = Pattern.compile("dsplayer\\.hotkeys[^']+'([^']+).+?function");
            Matcher matcher = pattern.matcher(response);
            if (matcher.find()) {

                urlt = "https://" + getHost(finalUrl) + matcher.group(1);


                pattern = Pattern.compile("makePlay.+?return[^?]+([^\"]+)");
                matcher = pattern.matcher(response);
                if (matcher.find()) {
                    token = matcher.group(1);

                    Connection.Response resp2 = Jsoup.connect("https://" + getHost(matcher.group(1)))
                            .method(Connection.Method.GET)
                            .userAgent(Constant.USER_AGENT)
                            .referrer(finalUrl)
                            .execute();

                    String response2 = resp2.body();

                    String test = response2 + randomStr(10) + token + System.currentTimeMillis() / 1000L;


                    System.out.println(test);

                }

            }
        }



        /*
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

        return Optional.empty();
    }

    private static String randomStr(int len) {
        final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom rnd = new SecureRandom();


        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();

    }

    private static String getHost(String uri) throws MalformedURLException {
        URL url = new URL(uri);

        return url.getHost();
    }

}
