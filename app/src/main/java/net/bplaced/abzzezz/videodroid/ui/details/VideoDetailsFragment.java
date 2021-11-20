package net.bplaced.abzzezz.videodroid.ui.details;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.app.DetailsSupportFragmentBackgroundController;
import androidx.leanback.widget.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import net.bplaced.abzzezz.videodroid.R;
import net.bplaced.abzzezz.videodroid.Viddroid;
import net.bplaced.abzzezz.videodroid.ui.error.BrowseErrorActivity;
import net.bplaced.abzzezz.videodroid.ui.main.MainActivity;
import net.bplaced.abzzezz.videodroid.ui.playback.PlaybackActivity;
import net.bplaced.abzzezz.videodroid.ui.presenter.CardPresenter;
import net.bplaced.abzzezz.videodroid.util.IntentHelper;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.ProviderProperties;
import net.bplaced.abzzezz.videodroid.util.provider.Providers;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import net.bplaced.abzzezz.videodroid.util.watchable.Watchable;
import net.bplaced.abzzezz.videodroid.util.watchable.models.Episode;
import net.bplaced.abzzezz.videodroid.util.watchable.models.Season;
import org.json.JSONArray;

import java.util.Objects;
import java.util.Optional;

import static net.bplaced.abzzezz.videodroid.util.math.MathUtil.convertDpToPixel;

/*
 * LeanbackDetailsFragment extends DetailsFragment, a Wrapper fragment for leanback details screens.
 * It shows a detailed view of video and its meta plus related videos.
 */
public class VideoDetailsFragment extends DetailsSupportFragment {

    private static final String TAG = "VideoDetailsFragment";

    private static final int DETAIL_THUMB_WIDTH = 300;
    private static final int DETAIL_THUMB_HEIGHT = 450;

    private Watchable mSelectedWatchable;

    private ArrayObjectAdapter mAdapter;
    private ClassPresenterSelector mPresenterSelector;

    private DetailsSupportFragmentBackgroundController mDetailsBackground;

    private ArrayObjectAdapter actionAdapter;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        Log.d(TAG, "onCreate DetailsFragment");
        super.onCreate(savedInstanceState);

        this.mDetailsBackground = new DetailsSupportFragmentBackgroundController(this);

        this.mSelectedWatchable = (Watchable) IntentHelper.getObjectForKey(DetailsActivity.WATCHABLE);

        if (mSelectedWatchable != null) {
            this.mPresenterSelector = new ClassPresenterSelector();
            this.mPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());

            this.mAdapter = new ArrayObjectAdapter(mPresenterSelector);
            this.actionAdapter = new ArrayObjectAdapter();

            setupDetailsOverviewRow();
            setupDetailsOverviewRowPresenter();

            setAdapter(mAdapter);
            initializeBackground(mSelectedWatchable);
            setOnItemViewClickedListener((itemViewHolder, item, rowViewHolder, row) -> {
                if (item instanceof Episode) {
                    playback((Episode) item);
                }
            });
        } else {
            final Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void initializeBackground(final Watchable data) {
        mDetailsBackground.enableParallax();
        Glide.with(getActivity())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.default_background)
                .load(data.getBackgroundImageUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        mDetailsBackground.setCoverBitmap(bitmap);
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private void setupDetailsOverviewRow() {
        assert getActivity() != null;
        assert getContext() != null;

        Log.d(TAG, "doInBackground: " + mSelectedWatchable.toString());
        final DetailsOverviewRow upperRow = new DetailsOverviewRow(mSelectedWatchable);
        upperRow.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.default_background));

        final int width = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_WIDTH);
        final int height = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_HEIGHT);

        Glide.with(getActivity())
                .load(mSelectedWatchable.getCardImageUrl())
                .centerCrop()
                .error(R.drawable.default_background)
                .into(new CustomTarget<Drawable>(width, height) {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                        Log.d(TAG, "details overview card image url ready: " + drawable);
                        upperRow.setImageDrawable(drawable);
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        this.setupActions(upperRow);
        this.setupRows();
    }

    private void setupActions(final DetailsOverviewRow upperRow) {
        final boolean isFavorite = Viddroid.INSTANCE.getWatchableList().isFavorite(mSelectedWatchable);
        final boolean isMarked = Viddroid.INSTANCE.getWatchableList().isMarked(mSelectedWatchable);

        actionAdapter.add(new Action(0, getString(R.string.favorite),
                isFavorite ? getString(R.string.remove_favorite) : getString(R.string.add_favorite),
                isFavorite ? getContext().getDrawable(R.drawable.outline_favorite_24) : getContext().getDrawable(R.drawable.outline_favorite_border_24))
        );

        actionAdapter.add(new Action(1, getString(R.string.bookmarked),
                isMarked ? getString(R.string.rem_bookmark) : getString(R.string.add_bookmark),
                isMarked ? getContext().getDrawable(R.drawable.outline_bookmark_24) : getContext().getDrawable(R.drawable.outline_bookmark_border_24)));

        upperRow.setActionsAdapter(actionAdapter);
        mAdapter.add(upperRow);
    }

    private void setupRows() {
        if (mSelectedWatchable instanceof TVShow) {
            final TVShow mSelectedTVShow = (TVShow) mSelectedWatchable;
            final CardPresenter cardPresenter = new CardPresenter();

            for (int i = 0; i < mSelectedTVShow.getSeasons().length(); i++) {
                final HeaderItem seasonHeader = new HeaderItem(i, "Season " + i);
                final ArrayObjectAdapter episodeListRow = new ArrayObjectAdapter(cardPresenter);

                final Optional<Season> value = mSelectedTVShow.getSeason(i);

                if (value.isPresent()) {
                    final Season season = value.get();

                    Optional<JSONArray> seasonEpisodesOptional = value.get().getSeasonEpisodes();

                    if (seasonEpisodesOptional.isPresent()) {
                        final JSONArray seasonEpisodes = seasonEpisodesOptional.get();
                        for (int j = 0; j < seasonEpisodes.length(); j++) {
                            final Episode episode = (Episode) seasonEpisodes.opt(j);

                            if (episode != null)
                                episodeListRow.add(episode.setIndices(new int[]{season.getSeasonNumber().orElse(i), j + 1}));
                        }
                    }
                }
                mAdapter.add(new ListRow(seasonHeader, episodeListRow));
            }
        } else {
            actionAdapter.add(new Action(69, "Play"));
        }
    }

    private void setupDetailsOverviewRowPresenter() {
        // Set detail background.
        final FullWidthDetailsOverviewRowPresenter detailsPresenter = new FullWidthDetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
        detailsPresenter.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.selected_background));

        // Hook up transition element.
        final FullWidthDetailsOverviewSharedElementHelper sharedElementHelper = new FullWidthDetailsOverviewSharedElementHelper();

        sharedElementHelper.setSharedElementEnterTransition(getActivity(), DetailsActivity.SHARED_ELEMENT_NAME);
        detailsPresenter.setListener(sharedElementHelper);
        detailsPresenter.setParticipatingEntranceTransition(true);

        detailsPresenter.setOnActionClickedListener(action -> {
            switch ((int) action.getId()) {
                case 0:
                    if (Viddroid.INSTANCE.getWatchableList().isFavorite(mSelectedWatchable)) {
                        Viddroid.INSTANCE.getWatchableList().removeFavorite(mSelectedWatchable);
                        action.setIcon(getContext().getDrawable(R.drawable.outline_favorite_border_24));
                        action.setLabel2(getString(R.string.add_favorite));
                    } else {
                        Viddroid.INSTANCE.getWatchableList().addFavorite(mSelectedWatchable);
                        action.setIcon(getContext().getDrawable(R.drawable.outline_favorite_24));
                        action.setLabel2(getString(R.string.remove_favorite));
                    }
                    actionAdapter.notifyArrayItemRangeChanged(0, 1);
                    break;
                case 1:
                    if (Viddroid.INSTANCE.getWatchableList().isMarked(mSelectedWatchable)) {
                        Viddroid.INSTANCE.getWatchableList().removeMarked(mSelectedWatchable);
                        action.setIcon(getContext().getDrawable(R.drawable.outline_bookmark_border_24));
                        action.setLabel2(getString(R.string.add_bookmark));
                    } else {
                        Viddroid.INSTANCE.getWatchableList().addMarked(mSelectedWatchable);
                        action.setIcon(getContext().getDrawable(R.drawable.outline_bookmark_24));
                        action.setLabel2(getString(R.string.rem_bookmark));
                    }
                    actionAdapter.notifyArrayItemRangeChanged(1, 2);
                    break;

                case 69:
                    playback(null);
                    break;
                default:
                    break;

            }
        });
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }

    private void playback(Episode episode) {
        if (episode == null && mSelectedWatchable instanceof TVShow)
            return;

        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

        //TODO:
        if (mSelectedWatchable instanceof TVShow)
            Providers
                    .resolveTV((TVShow) mSelectedWatchable,
                            episode,
                            ProviderProperties.STREAMS_TV,
                            urlConnection -> startPlayback(urlConnection, mDialog),
                            exception -> showErrorFragment(mDialog));
        else
            Providers.resolveMovie((Movie) mSelectedWatchable,
                    ProviderProperties.STREAMS_MOVIE,
                    urlConnection -> startPlayback(urlConnection, mDialog),
                    exception -> showErrorFragment(mDialog));
    }

    private void showErrorFragment(final ProgressDialog progressDialog) {
        progressDialog.cancel();
        final Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
        startActivity(intent);
    }

    private void startPlayback(final Optional<ParcelableWatchableURLConnection> parcelableMovieURLConnection, final ProgressDialog progressDialog) {
        if (parcelableMovieURLConnection.isPresent()) {
            final Intent intent = new Intent(getActivity(), PlaybackActivity.class);
            IntentHelper.addObjectForKey(mSelectedWatchable, DetailsActivity.WATCHABLE);
            intent.putExtra(DetailsActivity.WATCHABLE_URL, parcelableMovieURLConnection.get());
            startActivity(intent);
        } else {
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                Toast.makeText(getActivity(), "Provider not available!", Toast.LENGTH_SHORT).show();
            });
        }
        progressDialog.cancel();
    }
}