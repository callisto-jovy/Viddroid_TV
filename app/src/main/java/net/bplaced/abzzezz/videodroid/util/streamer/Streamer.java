package net.bplaced.abzzezz.videodroid.util.streamer;

import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import org.json.JSONException;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public abstract class Streamer {

    public abstract Optional<ParcelableWatchableURLConnection> resolveStreamURL(final String referral, final Optional<Map<String, String>> headers) throws IOException, JSONException;

}
