package net.bplaced.abzzezz.videodroid.util.string;


import java.util.regex.Pattern;

public class StringUtil {

    public static final String ALLOWED_URL_SPECIAL_CHARS = "[^$–_.+!*‘(),\\p{L}\\p{N}]";

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
}
