package net.bplaced.abzzezz.videodroid.util.task.tasks.themoviedb;

import net.bplaced.abzzezz.videodroid.util.connection.URLUtil;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.themoviedb.TheMovieDB;
import net.bplaced.abzzezz.videodroid.util.themoviedb.TheMovieDBAPIEndpoint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.concurrent.Callable;

public class TheMovieDBMovieSearchTask extends TaskExecutor implements Callable<JSONArray>, TheMovieDB {

    private final String searchQuery;

    public TheMovieDBMovieSearchTask(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void executeAsync(Callback<JSONArray> callback) {
        super.executeAsync(this, callback);
    }

    @Override
    public JSONArray call() throws Exception {
        final HttpURLConnection connection = URLUtil.createHTTPURLConnection(this.formatEndpointSearchRequest(TheMovieDBAPIEndpoint.SEARCH_MOVIE, searchQuery));
        final JSONObject resp = new JSONObject(URLUtil.collectLines(connection));

        return resp.getJSONArray("results");
    }
}
