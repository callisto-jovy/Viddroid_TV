package net.bplaced.abzzezz.videodroid.util.streamer.streamer.vidcloud;

import java.util.concurrent.ThreadLocalRandom;

public interface VidCloudCCAPI {


    String AES_KEY = "25746538592938496764662879833288";
    String BASE_URL = "https://vidembed.io/";

    default String formatAPIURL(String id, String time) {
        return "https://vidembed.io/encrypt-ajax.php?id=" + id + "&refer=none&time=" + time;
    }

    default String rand(int iter) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < iter; i++) {
            s.append(ThreadLocalRandom.current().nextInt(9));
        }
        return s.toString();
    }


}
