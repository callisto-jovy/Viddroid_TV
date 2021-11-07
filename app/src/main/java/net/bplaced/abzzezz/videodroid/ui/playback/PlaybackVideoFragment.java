package net.bplaced.abzzezz.videodroid.ui.playback;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.media.PlaybackTransportControlGlue;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import net.bplaced.abzzezz.videodroid.ui.details.DetailsActivity;
import net.bplaced.abzzezz.videodroid.util.Constant;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.string.StringUtil;
import net.bplaced.abzzezz.videodroid.util.watchable.Watchable;

/**
 * Handles video playback with media controls.
 */
public class PlaybackVideoFragment extends VideoSupportFragment {

    private PlaybackTransportControlGlue<LeanbackPlayerAdapter> mTransportControlGlue;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Watchable watchable = (Watchable) getActivity().getIntent().getSerializableExtra(DetailsActivity.WATCHABLE);
        final ParcelableWatchableURLConnection parcelableWatchableURLConnection = getActivity().getIntent().getParcelableExtra(DetailsActivity.WATCHABLE_URL);

        final VideoSupportFragmentGlueHost glueHost = new VideoSupportFragmentGlueHost(PlaybackVideoFragment.this);

        final SimpleExoPlayer simpleExoPlayer = new SimpleExoPlayer.Builder(getContext())
                .setLoadControl(getLoadControl())
                .build();
        //Exo player
        simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);


        final LeanbackPlayerAdapter player = new LeanbackPlayerAdapter(getContext(), simpleExoPlayer, 1000);

        this.mTransportControlGlue = new PlaybackTransportControlGlue<>(getContext(), player);

        mTransportControlGlue.setSeekEnabled(true);
        mTransportControlGlue.setHost(glueHost);
        mTransportControlGlue.setTitle(watchable.getTitle());
        mTransportControlGlue.setSubtitle(watchable.getDescription());
        mTransportControlGlue.playWhenPrepared();

        /* HTTP(s)-Request */

        final DataSource.Factory defaultHttpDataSource = new DefaultHttpDataSource.Factory()
                .setDefaultRequestProperties(parcelableWatchableURLConnection.getHeaders())
                .setConnectTimeoutMs(parcelableWatchableURLConnection.getConnectTimeout())
                .setReadTimeoutMs(parcelableWatchableURLConnection.getReadTimeout())
                .setUserAgent(Constant.USER_AGENT);


        final MediaSource videoSource;

        final String url = parcelableWatchableURLConnection.getUrl();
        Log.d("PlaybackVideoFragment URL", url);

        if (StringUtil.getFileNameFromURI(url).endsWith(".m3u8")) {
            videoSource = new HlsMediaSource.Factory(defaultHttpDataSource)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(url)));
        } else {
            videoSource = new ProgressiveMediaSource.Factory(defaultHttpDataSource)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(url)));
        }

        simpleExoPlayer.addMediaSource(videoSource);
        simpleExoPlayer.seekTo(0);
        simpleExoPlayer.prepare();
    }


    private LoadControl getLoadControl() {
        DefaultLoadControl.Builder builder = new DefaultLoadControl.Builder();

        builder.setBufferDurationsMs(
                50000, // DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                50000, //*.DEFAULT_MAX_BUFFER_MS,
                1000, // *.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                1000 // *.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
        );
        return builder.build();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mTransportControlGlue != null) {
            mTransportControlGlue.pause();
        }
    }
}