package net.bplaced.abzzezz.videodroid.util.provider;

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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static net.bplaced.abzzezz.videodroid.util.provider.ProviderProperties.*;

public enum Providers {

    API_DB(new ApiMdb(), STREAMS_MOVIE & STREAMS_TV & LANGUAGE_ENGLISH),
    GOOGL_VIDEO(new GooglVideo(), STREAMS_TV & LANGUAGE_ENGLISH),
    MOVIES_CO(new MoviesCo(), STREAMS_MOVIE & STREAMS_TV & LANGUAGE_ENGLISH),
    BEST_MOVIES_WATCH(new BestMoviesWatch(), STREAMS_MOVIE & LANGUAGE_ENGLISH),
    MOVIE_TECH_HUB(new MoviesHubTech(), STREAMS_MOVIE & LANGUAGE_ENGLISH),
    VIDEOVAK_COM(new VideovakCom(), STREAMS_TV & LANGUAGE_ENGLISH);

    private final int providerProperties;
    private final Provider provider;

    Providers(final Provider provider, final int providerProperties) {
        this.provider = provider;
        this.providerProperties = providerProperties;
    }

    public int getProviderProperties() {
        return providerProperties;
    }

    public static void resolveTV(final TVShow watchable,
                                 Episode episode,
                                 final int filter,
                                 final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer,
                                 final Consumer<String> exceptionConsumer) {
        resolveTV(watchable, episode, filter, new ArrayList<>(Arrays.asList(Providers.values())), directConnectionConsumer, exceptionConsumer);
    }

    private static void resolveTV(final TVShow watchable,
                                  Episode episode,
                                  final int filter,
                                  final List<Providers> providers,
                                  final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer,
                                  final Consumer<String> exceptionConsumer) {
        providers.stream().filter(p -> p.getProviderProperties() == filter)
                .findFirst()
                .ifPresent(p -> {
                    p.getProvider().requestTVLink(watchable, episode, parcelableWatchableURLConnection -> {
                        if (parcelableWatchableURLConnection.isPresent()) {
                            directConnectionConsumer.accept(parcelableWatchableURLConnection);
                        } else {
                            providers.remove(p); //Exclude provider
                            resolveTV(watchable, episode, filter, providers, directConnectionConsumer, exceptionConsumer);
                        }
                    }, exceptionConsumer);
                });

    }


    private static void resolveMovie(final Movie watchable,
                                     final int filter,
                                     final List<Providers> providers,
                                     final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer,
                                     final Consumer<String> exceptionConsumer) {
        providers.stream().filter(p -> p.getProviderProperties() == filter)
                .findFirst()
                .ifPresent(p -> {
                    p.getProvider().requestMovieLink(watchable, parcelableWatchableURLConnection -> {
                        if (parcelableWatchableURLConnection.isPresent()) {
                            directConnectionConsumer.accept(parcelableWatchableURLConnection);
                        } else {
                            providers.remove(p); //Exclude provider
                            resolveMovie(watchable, filter, providers, directConnectionConsumer, exceptionConsumer);
                        }
                    }, exceptionConsumer);
                });
    }

    public static void resolveMovie(final Movie watchable,
                                    final int filter,
                                    final Consumer<Optional<ParcelableWatchableURLConnection>> directConnectionConsumer,
                                    final Consumer<String> exceptionConsumer) {
        resolveMovie(watchable, filter, new ArrayList<>(Arrays.asList(Providers.values())), directConnectionConsumer, exceptionConsumer);
    }


    public Provider getProvider() {
        return provider;
    }
}

