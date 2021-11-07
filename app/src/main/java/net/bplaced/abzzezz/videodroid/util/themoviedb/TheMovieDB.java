package net.bplaced.abzzezz.videodroid.util.themoviedb;

import net.bplaced.abzzezz.videodroid.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public interface TheMovieDB {

    String API_V3_ENDPOINT = "https://api.themoviedb.org/3/";
    String API_V3_SEARCH_ENDPOINT_FORMAT = "https://api.themoviedb.org/3%s?api_key=%s&page=1&query=%s";
    String API_V3_ENDPOINT_FORMAT = "https://api.themoviedb.org/3%s/%s?api_key=%s";


    String API_IMAGE_ENDPOINT = "https://image.tmdb.org/t/p/%s%s";
    String API_KEY = BuildConfig.TheMovieDBAPIKey;

    String[] IMAGE_WIDTH = {
            "w300",
            "w500",
            "original"
    };

    default String formatEndpointSearchRequest(final TheMovieDBAPIEndpoint apiEndpoint, final String query) throws UnsupportedEncodingException {
        return String.format(API_V3_SEARCH_ENDPOINT_FORMAT, apiEndpoint.getEndpoint(), API_KEY, URLEncoder.encode(query, StandardCharsets.UTF_8.name()));
    }

    default String formatRequest(final TheMovieDBAPIEndpoint apiEndpoint, final String query) {
        return String.format(API_V3_ENDPOINT_FORMAT, apiEndpoint.getEndpoint(), query, API_KEY);
    }


    default String formatPosterPath(final String posterPath, final String width) {
        return String.format(API_IMAGE_ENDPOINT, width, posterPath);
    }
}
