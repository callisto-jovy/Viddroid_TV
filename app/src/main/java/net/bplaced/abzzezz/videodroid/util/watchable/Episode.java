package net.bplaced.abzzezz.videodroid.util.watchable;

public class Episode {

    private final int index, season;
    private final String seasonPoster;

    public Episode(int index, int season, String seasonPoster) {
        this.index = index;
        this.season = season;
        this.seasonPoster = seasonPoster;
    }

    public int getSeason() {
        return season;
    }

    public int getIndex() {
        return index;
    }

    public String getSeasonPoster() {
        return seasonPoster;
    }
}
