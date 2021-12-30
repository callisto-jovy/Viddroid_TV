package net.bplaced.abzzezz.videodroid.util.provider;

import android.util.Log;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.providers.apimdb.ApiMdb;
import net.bplaced.abzzezz.videodroid.util.provider.providers.bestmovies.BestMoviesWatch;
import net.bplaced.abzzezz.videodroid.util.provider.providers.googlvideo.GooglVideo;
import net.bplaced.abzzezz.videodroid.util.provider.providers.moviesco.MoviesCo;
import net.bplaced.abzzezz.videodroid.util.provider.providers.movieshub.MoviesHubTech;
import net.bplaced.abzzezz.videodroid.util.provider.providers.videovak.VideovakCom;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import net.bplaced.abzzezz.videodroid.util.watchable.models.Episode;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static net.bplaced.abzzezz.videodroid.util.provider.ProviderProperties.*;

public enum Providers {

    API_DB(new ApiMdb(), EnumSet.of(STREAMS_TV, STREAMS_MOVIE, LANGUAGE_ENGLISH)),
    GOOGL_VIDEO(new GooglVideo(), EnumSet.of(STREAMS_TV, LANGUAGE_ENGLISH)),
    MOVIES_CO(new MoviesCo(), EnumSet.of(STREAMS_TV, STREAMS_MOVIE, LANGUAGE_ENGLISH)),
    BEST_MOVIES_WATCH(new BestMoviesWatch(), EnumSet.of(STREAMS_MOVIE, LANGUAGE_ENGLISH)),
    MOVIE_TECH_HUB(new MoviesHubTech(), EnumSet.of(STREAMS_MOVIE, LANGUAGE_ENGLISH)),
    VIDEOVAK_COM(new VideovakCom(), EnumSet.of(STREAMS_MOVIE, LANGUAGE_ENGLISH));

    private final EnumSet<ProviderProperties> properties;
    private final Provider provider;

    Providers(final Provider provider, final EnumSet<ProviderProperties> providerProperties) {
        this.provider = provider;
        this.properties = providerProperties;
    }

    public EnumSet<ProviderProperties> getProperties() {
        return properties;
    }

    private static List<Providers> compileProviders(final EnumSet<ProviderProperties> mask) {
        List<Providers> providers = new ArrayList<>();
        for (Providers value : Providers.values()) {
            if (value.getProperties().containsAll(mask)) {
                providers.add(value);
            }
        }
        Log.i("Providers", providers.toString());
        return providers;
    }


    private static void resolveTV(final TVShow watchable,
                                  Episode episode,
                                  final List<Providers> providers,
                                  final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer,
                                  final Consumer<String> exceptionConsumer) {

        if (providers.size() > 1) {
            final Providers provider = providers.get(0);
            provider.getProvider().requestTVLink(watchable, episode, urlConnection -> {
                if (urlConnection.isPresent()) {
                    directConnectionConsumer.accept(urlConnection);
                } else {
                    providers.remove(provider); //Exclude provider
                    resolveTV(watchable, episode, providers, directConnectionConsumer, exceptionConsumer);
                }
            }, exceptionConsumer);
        }
    }


    private static void resolveMovie(final Movie watchable,
                                     final List<Providers> providers,
                                     final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer,
                                     final Consumer<String> exceptionConsumer) {
        if (providers.size() > 1) {
            final Providers provider = providers.get(0);

            provider.getProvider().requestMovieLink(watchable, urlConnection -> {
                if (urlConnection.isPresent()) {
                    directConnectionConsumer.accept(urlConnection);
                } else {
                    providers.remove(provider); //Exclude provider
                    resolveMovie(watchable, providers, directConnectionConsumer, exceptionConsumer);
                }
            }, exceptionConsumer);
        }
    }

    public static void resolveTV(final TVShow watchable,
                                 Episode episode,
                                 final EnumSet<ProviderProperties> filter,
                                 final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer,
                                 final Consumer<String> exceptionConsumer) {
        resolveTV(watchable, episode, compileProviders(filter), directConnectionConsumer, exceptionConsumer);
    }

    public static void resolveMovie(final Movie watchable,
                                    final EnumSet<ProviderProperties> filter,
                                    final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer,
                                    final Consumer<String> exceptionConsumer) {
        resolveMovie(watchable, compileProviders(filter), directConnectionConsumer, exceptionConsumer);
    }


    public Provider getProvider() {
        return provider;
    }
}

