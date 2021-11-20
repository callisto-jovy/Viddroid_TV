package net.bplaced.abzzezz.videodroid.util.watchable.models;

import net.bplaced.abzzezz.videodroid.util.themoviedb.TheMovieDB;
import net.bplaced.abzzezz.videodroid.util.ui.CardPresentable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class Episode extends CardPresentable implements TheMovieDB {

    private final String episodeName;
    private final String episodeId;
    private final String episodePosterPath;

    private int[] indices;

    public Episode(String episodeName, String episodeId, String episodePosterPath) {
        super(episodeName == null || episodeName.isEmpty() ? "Episode" : episodeName);
        this.episodeName = episodeName;
        this.episodeId = episodeId;
        this.episodePosterPath = episodePosterPath;
        this.setPoster(getCardImageUrl());
    }


    public Episode(JSONObject jsonObject) {
        this.episodeName = jsonObject.optString("ep_name");
        this.episodePosterPath = jsonObject.optString("ep_poster_path");
        this.episodeId = jsonObject.optString("ep_id");
        this.setPoster(getCardImageUrl());
        this.setCardTitleText(episodeName);
    }

    public JSONObject serializeEpisode() {
        try {
            return new JSONObject()
                    .put("ep_name", episodeName)
                    .put("ep_poster_path", episodePosterPath)
                    .put("ep_id", episodeId);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public String getCardImageUrl() {
        if (episodePosterPath == null)
            return null;
        else
            return formatPosterPath(episodePosterPath, IMAGE_WIDTH[0]);
    }

    public Optional<String> getEpisodeName() {
        return Optional.of(episodeName);
    }

    public Optional<String> getEpisodePosterPath() {
        return Optional.of(episodePosterPath);
    }

    public Optional<String> getEpisodeId() {
        return Optional.of(episodeId);
    }

    public int[] getIndices() {
        return indices;
    }

    public Episode setIndices(int[] indices) {
        this.indices = indices;
        return this;
    }
}
