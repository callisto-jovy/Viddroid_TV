package net.bplaced.abzzezz.videodroid.util.watchable;

import net.bplaced.abzzezz.videodroid.util.watchable.models.Episode;
import net.bplaced.abzzezz.videodroid.util.watchable.models.Season;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class TVShow extends Watchable {

    private final JSONArray seasons; //JSON Array with seasons

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
            final JSONArray serializedSeasons = jsonObject.getJSONArray("seasons");
            final JSONArray seasons = new JSONArray();

            for (int i = 0; i < serializedSeasons.length(); i++) {
                final JSONObject seasonObject = serializedSeasons.optJSONObject(i);
                if (seasonObject == null)
                    continue;
                final Season season = new Season(seasonObject);


                final JSONArray episodeArray = new JSONArray();
                season.getSeasonEpisodes().ifPresent(jsonArray -> {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        final JSONObject episodeObject = jsonArray.optJSONObject(j);
                        if (episodeObject == null)
                            continue;
                        final Episode episode = new Episode(episodeObject);
                        episodeArray.put(episode);
                    }
                });
                season.setSeasonEpisodes(episodeArray);
                seasons.put(season);
            }
            this.seasons = seasons;
        } else {
            this.seasons = new JSONArray();
        }
    }

    public void addSeason(int index, Season season) {
        try {
            seasons.put(index, season);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getEpisodePosterPath(int season, int episode) {
        Optional<String> s = Optional.ofNullable(seasons.opt(season))
                .flatMap(object -> ((Season) object).getSeasonEpisodes())
                .map(jsonArray -> jsonArray.opt(episode))
                .flatMap(object -> ((Episode) object).getEpisodePosterPath());

        return s.map(value -> formatPosterPath(value, IMAGE_WIDTH[2]))
                .orElse("https://www.themoviedb.org/assets/2/apple-touch-icon-cfba7699efe7a742de25c28e08c38525f19381d31087c69e89d6bcb8e3c0ddfa.png");
    }

    public JSONArray getSeasons() {
        return seasons;
    }

    public Optional<Season> getSeason(int index) {
        return Optional.ofNullable((Season) seasons.opt(index));
    }

    @Override
    public JSONObject serializeWatchable() throws JSONException {
        final JSONArray seasonsSerialized = new JSONArray();

        for (int i = 0; i < seasons.length(); i++) {
            final Season season = (Season) seasons.opt(i);
            if (season != null) {

                final JSONArray episodesSerialized = new JSONArray();

                season.getSeasonEpisodes().ifPresent(episodes -> {
                    for (int j = 0; j < episodes.length(); j++) {
                        final Episode episode = (Episode) episodes.opt(j);
                        if (episode != null) {
                            episodesSerialized.put(episode.serializeEpisode());
                        }
                    }
                });
                seasonsSerialized.put(season.serializeSeason(episodesSerialized));
            }
        }

        return new JSONObject()
                .put("id", getId())
                .put("name", getTitle())
                .put("overview", getDescription())
                .put("backdrop_path", getBackdropPath())
                .put("poster_path", getCardImagePath())
                .put("seasons", seasonsSerialized)
                .put("ser", 1)
                .put("tv", true);
    }
}
