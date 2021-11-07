package net.bplaced.abzzezz.videodroid.util.watchable;

import net.bplaced.abzzezz.videodroid.util.themoviedb.TheMovieDB;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public abstract class Watchable implements Serializable, TheMovieDB {

    static final long serialVersionUID = 727566175075960653L;

    private final int id;
    private final String title, description, backdropPath, cardImagePath;

    public Watchable(final int id, final String title, final String description, final String backdropPath, final String cardImagePath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.backdropPath = backdropPath;
        this.cardImagePath = cardImagePath;
    }

    protected String getBackdropPath() {
        return backdropPath;
    }

    protected String getCardImagePath() {
        return cardImagePath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBackgroundImageUrl() {
        if (backdropPath == null)
            return null;
        else
            return formatPosterPath(backdropPath, IMAGE_WIDTH[2]);
    }

    public String getCardImageUrl() {
        if (cardImagePath == null)
            return null;
        else
            return formatPosterPath(cardImagePath, IMAGE_WIDTH[0]);
    }

    public abstract JSONObject serializeWatchable() throws JSONException;

}
