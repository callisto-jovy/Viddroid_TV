package net.bplaced.abzzezz.videodroid.util.provider.providers.vidcloud;

import net.bplaced.abzzezz.videodroid.util.string.StringUtil;

import java.util.Locale;

public interface VidCloudCCAPI {


    String BASE_URL = "https://vidembed.cc";
    String EPISODE_ENDPOINT = "/videos/";

    String TV_FORMAT = "%s season %d episode %d %s";


    default String formatTVRequest(final String tvTitle, final String episodeTitle, int season, int episode) {
        return BASE_URL + EPISODE_ENDPOINT + StringUtil.dashes(String.format(Locale.ENGLISH, TV_FORMAT, tvTitle, season, episode, episodeTitle));
    }

    default String formatMovieRequest(final String title) { //If it does not work add 1080p
        return BASE_URL + EPISODE_ENDPOINT + StringUtil.dashes(title) + "-hd-720p";
    }

}
