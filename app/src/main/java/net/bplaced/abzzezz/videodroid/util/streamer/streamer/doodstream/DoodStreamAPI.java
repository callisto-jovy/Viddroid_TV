package net.bplaced.abzzezz.videodroid.util.streamer.streamer.doodstream;

import java.time.LocalDate;
import java.util.regex.Pattern;

public interface DoodStreamAPI {

    String BASE_URL = "https://dood.ws/";
    Pattern PASS_MD_5_PATTERN = Pattern.compile("(?<=')/pass_md5(.*)(?=')");

    String TOKEN = "g603no0e44n4cg2469vebere";

    default String generateToken() {
        StringBuilder a = new StringBuilder();
        String t = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int n = t.length(), o = 0; 10 > o; o++) {
            a.append(t.charAt((int) Math.floor(Math.random() * n)));
        }
        return a + "?token=" + TOKEN + "&expiry=" + LocalDate.now();
    }

}
