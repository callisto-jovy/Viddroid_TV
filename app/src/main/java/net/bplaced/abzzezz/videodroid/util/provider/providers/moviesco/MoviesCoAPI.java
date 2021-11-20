package net.bplaced.abzzezz.videodroid.util.provider.providers.moviesco;

import net.bplaced.abzzezz.videodroid.util.string.StringUtil;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;

import java.util.regex.Pattern;

public interface MoviesCoAPI {

    String BASE_URL = "https://www1.123movies.co";
    String MOVIE_ENDPOINT = "/movie/";
    String TV_ENDPOINT = "/episode/";

    String DECODING_API = "https://gomo.to/decoding_v3.php";

    Pattern TC_PATTERN = Pattern.compile("var tc = '(.*)'");
    Pattern TOKEN_PATTERN = Pattern.compile("\"_token\": \"(.*)\"");
    Pattern FUNCTION_PATTERN = Pattern.compile("function (.*? \\{ (.*)+\\})", Pattern.MULTILINE);
    //Pattern UNPACKER_PATTERN = Pattern.compile("eval\\(function\\(p,a,c,k,e,d\\)(.*)+\\)");
    Pattern SOURCE_PATTERN = Pattern.compile("(?<=sources:\\[\\{file:\")[^\"]+");

    Pattern ONCLICK_PATTERN = Pattern.compile("onclick=\"download_video\\('([^']+)','([^']+)','([^']+)'\\)", Pattern.MULTILINE);
    Pattern DIRECT_DOWNLOAD_LINK_PATTERN = Pattern.compile("<a href=\"([^\"]+)\">Direct Download Link</a>", Pattern.MULTILINE);


    Pattern SLICE_PATTERN = Pattern.compile("slice\\((\\d),(\\d+)\\)");

    Pattern RND_NUM_PATTERN = Pattern.compile("\\+ \"(\\d+)\"\\+\"(\\d+)");


    default String formatTvRequest(final TVShow tvShow, final int season, final int episode) {
        //https://www1.123movies.co/episode/peaky-blinders-1x2/watching.html
        return BASE_URL + TV_ENDPOINT + StringUtil.dashes(tvShow.getTitle()) + "-" + season + "x" + episode + "/watching.html";
    }

    default String formatMovieRequest(final String title) {
        return BASE_URL + MOVIE_ENDPOINT + StringUtil.dashes(title) + "/watching.html";
    }
}
