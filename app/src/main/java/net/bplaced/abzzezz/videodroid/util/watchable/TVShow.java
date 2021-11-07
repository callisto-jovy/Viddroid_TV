package net.bplaced.abzzezz.videodroid.util.watchable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TVShow extends Watchable {

    private int[] seasons; //length = seasons, value = episodes in season
    private String[] seasonPosterPaths; //poster paths for each individual season; length should be the length of seasons
    private int lastInsert; //TODO: Add

    public TVShow(final JSONObject jsonObject) throws JSONException {
        super(
                jsonObject.getInt("id"),
                jsonObject.optString("name", "Title unavailable"), //Tf Themoviedb, are u smoking crack?
                jsonObject.optString("overview", "Description unavailable"),
                jsonObject.getString("backdrop_path"),
                jsonObject.getString("poster_path")
        );
        if (jsonObject.has("ser")) {
            final JSONArray episodesJSONArray = jsonObject.getJSONArray("episodes");
            final JSONArray seasonPostersJSONArray = jsonObject.getJSONArray("season_posters");

            this.seasons = new int[episodesJSONArray.length()];
            this.seasonPosterPaths = new String[seasonPostersJSONArray.length()];

            for (int i = 0; i < seasons.length; i++) {
                this.seasons[i] = episodesJSONArray.getInt(i);
            }

            for (int i = 0; i < seasonPosterPaths.length; i++) {
                seasonPosterPaths[i] = seasonPostersJSONArray.getString(i);
            }
        }
    }


    public void addSeason(int index, int value, int initialLength) {
        if (seasons == null) seasons = new int[initialLength];
        seasons[index] = (value);
    }

    public void addSeasonPoster(int index, String value, int initialLength) {
        if (seasonPosterPaths == null) seasonPosterPaths = new String[initialLength];
        seasonPosterPaths[index] = (value);
    }

    public boolean isSeasonsNull() {
        return seasonPosterPaths == null || seasons == null;
    }

    public String getSeasonPoster(int index) {
        final String s = seasonPosterPaths[index];
        if (s == null)
            return null;
        else
            return formatPosterPath(s, IMAGE_WIDTH[2]);
    }

    private String[] getSeasonPosterPaths() {
        return seasonPosterPaths;
    }

    public int[] getSeasons() {
        return seasons;
    }

    @Override
    public JSONObject serializeWatchable() throws JSONException {
        final JSONArray seasons = new JSONArray();
        final JSONArray seasonPosters = new JSONArray();

        for (int i = 0; i < getSeasonPosterPaths().length; i++) {
            seasonPosters.put(getSeasonPosterPaths()[i]);
        }

        for (int i = 0; i < getSeasons().length; i++) {
            seasons.put(getSeasons()[i]);
        }

        return new JSONObject()
                .put("id", getId())
                .put("name", getTitle())
                .put("overview", getDescription())
                .put("backdrop_path", getBackdropPath())
                .put("poster_path", getCardImagePath())
                .put("episodes", seasons)
                .put("season_posters", seasonPosters)
                .put("ser", 1)
                .put("tv", true);
    }
}
