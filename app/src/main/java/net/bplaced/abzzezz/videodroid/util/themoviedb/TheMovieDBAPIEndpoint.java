package net.bplaced.abzzezz.videodroid.util.themoviedb;

public enum TheMovieDBAPIEndpoint {


    SEARCH_MOVIE("/search/movie"),
    SEARCH_TV("/search/tv"),
    SEARCH_MULTI("/search/multi"),
    TV_DETAILS("/tv");


    private final String endpoint;

    TheMovieDBAPIEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
