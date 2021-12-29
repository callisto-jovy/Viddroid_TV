package net.bplaced.abzzezz.videodroid.util.string;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class StringUtil {

    public static final String ALLOWED_URL_SPECIAL_CHARS = "[^–\\p{L}\\p{N}]";

    public static String dashes(final String string) {
        return Pattern.compile(ALLOWED_URL_SPECIAL_CHARS)
                .matcher(string)
                .replaceAll(" ")
                .replaceAll("\\s+", "-");
    }

    public static String underscores(final String string) {
        return Pattern.compile(ALLOWED_URL_SPECIAL_CHARS)
                .matcher(string)
                .replaceAll(" ")
                .replaceAll("\\s+", "_");
    }

    public static String getFileNameFromURI(final String path) {
        return path.substring(path.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0];
    }

    /**
     * Taken from: https://stackoverflow.com/questions/5902090/how-to-extract-parameters-from-a-given-url
     *
     * @param url url to get params from
     * @return Map with parameters
     */
    public static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    List<String> values = params.computeIfAbsent(key, k -> new ArrayList<>());
                    values.add(value);
                }
            }

            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }
}
