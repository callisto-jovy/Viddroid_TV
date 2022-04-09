package net.bplaced.abzzezz.videodroid.util.streamer.streamer.doodstream;

import java.util.regex.Pattern;

public interface DoodStreamAPI {

    String BASE_URL = "https://dood.watch/";
    Pattern PASS_MD_5_PATTERN = Pattern.compile("(?<=')/pass_md5(.*)(?=')");

    default String generateSuffix(final String token) {

        final StringBuilder a = new StringBuilder();
        final String t = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int n = t.length(), o = 0; 10 > o; o++) {
            a.append(t.charAt((int) Math.floor(Math.random() * n)));
        }
        return a + "?token=" + token + "&expiry=" + System.currentTimeMillis() / 1000L;
    }

}
