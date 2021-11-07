package net.bplaced.abzzezz.videodroid.util.provider;

import net.bplaced.abzzezz.videodroid.util.provider.providers.bestmovieswatch.BestMoviesWatch;
import net.bplaced.abzzezz.videodroid.util.provider.providers.moviesco.MoviesCo;
import net.bplaced.abzzezz.videodroid.util.provider.providers.movieshubtech.MoviesHubTech;
import net.bplaced.abzzezz.videodroid.util.provider.providers.videovakcom.VideovakCom;

public enum Providers {

    BEST_MOVIES_WATCH("English - Movies only", new BestMoviesWatch()),
    MOVIE_TECH_HUB("English - Movies only", new MoviesHubTech()),
    MOVIES_CO("English", new MoviesCo()),
    VIDEOVAK_COM("English - TV only", new VideovakCom());

    private final String language;
    private final Provider provider;

    Providers(final String language, final Provider provider) {
        this.language = language;
        this.provider = provider;
    }

    public Provider getProvider() {
        return provider;
    }

    public String getLanguage() {
        return language;
    }
}
