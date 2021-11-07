package net.bplaced.abzzezz.videodroid.util.provider.providers.videovakcom;

import net.bplaced.abzzezz.videodroid.util.string.StringUtil;

import java.util.Locale;

public interface VideovakComAPI {

    String BASE_URL = "https://videovak.com/en/";

    String SHOW_URL_FORMAT = "https://videovak.com/en/series/%s/S%dE%d/";

    String STREAM_API_RESOLVER_ENDPOINT = "https://stream.videovak.com/testapp/realtimeencoderstream_id.jsp";

    String STREAM_API_STREAM_FORMAT_ENDPOINT = "https://stream.videovak.com/testapp/torrentrealtimeencoderstream_v1.23.jsp?id=";

    String BASE_TEMP_FORMAT = "%s s%se%s";

    default String formatTVRequest(final String title, final int season, final int episode) {
        return String.format(Locale.ENGLISH, SHOW_URL_FORMAT, StringUtil.dashes(title), season, episode);
    }

    default String formatStreamTemp(final String title, final int season, final int episode) {
        return StringUtil.underscores(String.format(Locale.ENGLISH, BASE_TEMP_FORMAT,
                title.toLowerCase(Locale.ROOT),
                String.format(Locale.ENGLISH, "%02d", season),
                String.format(Locale.ENGLISH, "%02d", episode)));
    }
}
