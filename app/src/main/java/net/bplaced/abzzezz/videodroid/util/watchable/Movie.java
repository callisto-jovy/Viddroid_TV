package net.bplaced.abzzezz.videodroid.util.watchable;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie extends Watchable {

    public Movie(final JSONObject jsonObject) throws JSONException {
        super(
                jsonObject.getInt("id"),
                jsonObject.optString("title", "Title unavailable"),
                jsonObject.optString("overview", "Description unavailable"),
                jsonObject.getString("backdrop_path"),
                jsonObject.getString("poster_path")
        );
    }

    @Override
    public JSONObject serializeWatchable() throws JSONException {
        return new JSONObject()
                .put("id", getId())
                .put("title", getTitle())
                .put("overview", getDescription())
                .put("backdrop_path", getBackdropPath())
                .put("poster_path", getCardImagePath())
                .put("tv", false);
    }
}
