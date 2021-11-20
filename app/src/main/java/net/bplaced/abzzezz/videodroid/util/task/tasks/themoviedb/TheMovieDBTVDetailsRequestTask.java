package net.bplaced.abzzezz.videodroid.util.task.tasks.themoviedb;

import net.bplaced.abzzezz.videodroid.util.connection.URLUtil;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.themoviedb.TheMovieDB;
import net.bplaced.abzzezz.videodroid.util.themoviedb.TheMovieDBAPIEndpoint;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import net.bplaced.abzzezz.videodroid.util.watchable.models.Episode;
import net.bplaced.abzzezz.videodroid.util.watchable.models.Season;
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

            final String seasonEndpoint = this.formatRequest(TheMovieDBAPIEndpoint.TV_DETAILS, mTVShow.getId() + "/season/" + seasonIndex);
            final JSONObject seasonEndpointResponseJSON = new JSONObject(URLUtil.collectLines(URLUtil.createHTTPSURLConnection(seasonEndpoint)));
            final JSONArray seasonEpisodesArray = seasonEndpointResponseJSON.getJSONArray("episodes");

            final Season season = new Season(
                    seasonEndpointResponseJSON.optString("id"),
                    seasonEndpointResponseJSON.optString("name"),
                    seasonIndex,
                    jsonObject.optInt("episode_count")
            );

            final JSONArray episodeArray = new JSONArray();

            for (int j = 0; j < seasonEpisodesArray.length(); j++) {
                final JSONObject jsonObject1 = seasonEpisodesArray.getJSONObject(j);

                final Episode episode = new Episode(
                        jsonObject1.optString("name"),
                        jsonObject1.optString("id"),
                        jsonObject1.optString("still_path")
                );

                episodeArray.put(j, episode);
            }

            season.setSeasonEpisodes(episodeArray);
            mTVShow.addSeason(seasonIndex, season);
        }
        return mTVShow;
    }
}
