package net.bplaced.abzzezz.videodroid.util.watchable.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class Season {

    private final String seasonId;
    private final String seasonName;
    private final int seasonNumber;
    private final int seasonEpisodeCount;
    private JSONArray seasonEpisodes;

    public Season(String seasonId, String seasonName, int seasonNumber, JSONArray seasonEpisodes, int seasonEpisodeCount) {
        this.seasonId = seasonId;
        this.seasonName = seasonName;
        this.seasonNumber = seasonNumber;
        this.seasonEpisodes = seasonEpisodes;
        this.seasonEpisodeCount = seasonEpisodeCount;
    }

    public Season(String seasonId, String seasonName, int seasonNumber, int seasonEpisodeCount) {
        this.seasonId = seasonId;
        this.seasonName = seasonName;
        this.seasonNumber = seasonNumber;
        this.seasonEpisodeCount = seasonEpisodeCount;
    }

    public Season(JSONObject jsonObject) {
        this.seasonId = jsonObject.optString("sn_id");
        this.seasonName = jsonObject.optString("sn_name");
        this.seasonNumber = jsonObject.optInt("sn_number");
        this.seasonEpisodes = jsonObject.optJSONArray("sn_episodes");
        this.seasonEpisodeCount = jsonObject.optInt("sn_ep_count");
    }

    public JSONObject serializeSeason() {
        try {
            return new JSONObject()
                    .put("sn_id", seasonId)
                    .put("sn_name", seasonName)
                    .put("sn_number", seasonNumber)
                    .put("sn_episodes", seasonEpisodes)
                    .put("sn_ep_count", seasonEpisodeCount);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public JSONObject serializeSeason(JSONArray jsonArray) {
        try {
            return new JSONObject()
                    .put("sn_id", seasonId)
                    .put("sn_name", seasonName)
                    .put("sn_number", seasonNumber)
                    .put("sn_episodes", jsonArray)
                    .put("sn_ep_count", seasonEpisodeCount)
                    ;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public Optional<String> getSeasonId() {
        return Optional.of(seasonId);
    }

    public Optional<String> getSeasonName() {
        return Optional.of(seasonName);
    }

    public Optional<Integer> getSeasonNumber() {
        return Optional.of(seasonNumber);
    }

    public Optional<JSONArray> getSeasonEpisodes() {
        return Optional.ofNullable(seasonEpisodes);
    }

    public void setSeasonEpisodes(JSONArray seasonEpisodes) {
        this.seasonEpisodes = seasonEpisodes;
    }

    public JSONArray getSeasonEpisodes0() {
        return seasonEpisodes;
    }

    public Optional<Integer> getSeasonEpisodeCount() {
        return Optional.of(seasonEpisodeCount);
    }
}
