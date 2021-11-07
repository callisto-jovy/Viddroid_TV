package net.bplaced.abzzezz.videodroid.util.task.tasks;

import net.bplaced.abzzezz.videodroid.util.connection.URLUtil;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.themoviedb.TheMovieDB;
import net.bplaced.abzzezz.videodroid.util.themoviedb.TheMovieDBAPIEndpoint;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.util.concurrent.Callable;

public class TheMovieDBTVDetailsRequestTask extends TaskExecutor implements Callable<TVShow>, TheMovieDB {

    private final TVShow mTVShow;

    public TheMovieDBTVDetailsRequestTask(final TVShow mTVShow) {
        this.mTVShow = mTVShow;
    }

    public void executeAsync(final Callback<TVShow> callback) {
        super.executeAsync(this, callback);
    }

    @Override
    public TVShow call() throws Exception {
        final String endpoint = this.formatRequest(TheMovieDBAPIEndpoint.TV_DETAILS, String.valueOf(mTVShow.getId()));

        final HttpsURLConnection urlConnection = URLUtil.createHTTPSURLConnection(endpoint);
        final JSONObject responseJSON = new JSONObject(URLUtil.collectLines(urlConnection));
        final JSONArray seasons = responseJSON.getJSONArray("seasons");

        for (int i = 0; i < seasons.length(); i++) {
            final JSONObject jsonObject = seasons.getJSONObject(i);
            final int seasonIndex = jsonObject.optInt("season_number", i);

            mTVShow.addSeason(seasonIndex, jsonObject.getInt("episode_count"), seasons.length() + 1);
            mTVShow.addSeasonPoster(seasonIndex, jsonObject.getString("poster_path"), seasons.length() + 1);
        }

        return mTVShow;
    }
}
