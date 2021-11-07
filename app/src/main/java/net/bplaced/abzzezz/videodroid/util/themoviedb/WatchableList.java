package net.bplaced.abzzezz.videodroid.util.themoviedb;

import android.content.Context;
import android.content.SharedPreferences;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import net.bplaced.abzzezz.videodroid.util.watchable.Watchable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class WatchableList {


    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private final List<Watchable> favorites = new ArrayList<>();
    private final List<Watchable> marked = new ArrayList<>();

    public WatchableList(final Context context) {
        this.sharedPreferences = context.getSharedPreferences("movies", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.loadFavorites();
        this.loadMarked();
    }

    public void loadFavorites() {
        final Optional<String> optionalString = Optional.ofNullable(sharedPreferences.getString("favorites", null));
        optionalString.ifPresent(string -> {
            try {
                final JSONArray favoritesJSONArray = new JSONArray(string);

                for (int i = 0; i < favoritesJSONArray.length(); i++) {
                    final JSONObject watchableObject = favoritesJSONArray.getJSONObject(i);
                    favorites.add(deserializeWatchable(watchableObject));
                }
            } catch (final JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadMarked() {
        final Optional<String> optionalString = Optional.ofNullable(sharedPreferences.getString("marked", null));
        optionalString.ifPresent(string -> {
            try {
                final JSONArray markedJSONArray = new JSONArray(string);

                for (int i = 0; i < markedJSONArray.length(); i++) {
                    final JSONObject watchableObject = markedJSONArray.getJSONObject(i);
                    marked.add(deserializeWatchable(watchableObject));
                }
            } catch (final JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    public void saveFavorites() {
        final JSONArray jsonArray = new JSONArray();
        favorites.forEach(watchable -> {
            try {
                jsonArray.put(watchable.serializeWatchable());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        editor.putString("favorites", jsonArray.toString());
        editor.commit();
    }

    public void saveMarked() {
        final JSONArray jsonArray = new JSONArray();
        marked.forEach(watchable -> {
            try {
                jsonArray.put(watchable.serializeWatchable());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        editor.putString("marked", jsonArray.toString());
        editor.commit();
    }


    public Watchable deserializeWatchable(final JSONObject jsonObject) throws JSONException, UnsupportedEncodingException {
        return jsonObject.optBoolean("tv") ? new TVShow(jsonObject) : new Movie(jsonObject);
    }

    public static final String[] WATCHABLE_CATEGORIES = {
            "Favorites",
            "Marked"
    };

    public List<Watchable> getFavorites() {
        return favorites;
    }

    public List<Watchable> getMarked() {
        return marked;
    }

    public List<Watchable> getList(int i) {
        return i == 0 ? getFavorites() : getMarked();
    }

    public boolean isFavorite(final Watchable mSelectedWatchable) {
        return favorites.stream().anyMatch(watchable -> watchable.getTitle().equals(mSelectedWatchable.getTitle()) || watchable.getCardImageUrl().equals(mSelectedWatchable.getCardImageUrl()));
    }

    public boolean isMarked(final Watchable mSelectedWatchable) {
        return marked.stream().anyMatch(watchable -> watchable.getTitle().equals(mSelectedWatchable.getTitle()) || watchable.getCardImageUrl().equals(mSelectedWatchable.getCardImageUrl()));
    }

    public void addFavorite(final Watchable watchable) {
        if (!isFavorite(watchable)) {
            favorites.add(watchable);
        }
        this.saveFavorites();
    }

    public void removeFavorite(final Watchable mSelectedWatchable) {
        favorites.removeIf(movie -> movie.getTitle().equalsIgnoreCase(mSelectedWatchable.getTitle()));
        this.saveFavorites();
    }


    public void removeMarked(final Watchable mSelectedWatchable) {
        marked.removeIf(movie -> movie.getTitle().equalsIgnoreCase(mSelectedWatchable.getTitle()));
        this.saveMarked();
    }


    public void addMarked(final Watchable mSelectedWatchable) {
        if (!isMarked(mSelectedWatchable)) {
            marked.add(mSelectedWatchable);
        }
        this.saveMarked();
    }
}