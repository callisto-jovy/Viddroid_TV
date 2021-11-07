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
import net.bplaced.abzzezz.videodroid.ui.main.MainActivity;
import net.bplaced.abzzezz.videodroid.ui.playback.PlaybackActivity;
import net.bplaced.abzzezz.videodroid.ui.presenter.CardEpisodePresenter;
import net.bplaced.abzzezz.videodroid.util.connection.ParcelableWatchableURLConnection;
import net.bplaced.abzzezz.videodroid.util.provider.Providers;
import net.bplaced.abzzezz.videodroid.util.watchable.Episode;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import net.bplaced.abzzezz.videodroid.util.watchable.Watchable;

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

    private int season, episode;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        Log.d(TAG, "onCreate DetailsFragment");
        super.onCreate(savedInstanceState);

        this.mDetailsBackground = new DetailsSupportFragmentBackgroundController(this);

        this.mSelectedWatchable = (Watchable) getActivity().getIntent().getSerializableExtra(DetailsActivity.WATCHABLE);

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
                    final Episode e = (Episode) item;
                    episode = e.getIndex() + 1;
                    season = e.getSeason();
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

        final boolean isFavorite = Viddroid.INSTANCE.getWatchableList().isFavorite(mSelectedWatchable);
        final boolean isMarked = Viddroid.INSTANCE.getWatchableList().isMarked(mSelectedWatchable);

        actionAdapter.add(new Action(0, getString(R.string.favorite),
                isFavorite ? getString(R.string.remove_favorite) : getString(R.string.add_favorite),
                isFavorite ? getContext().getDrawable(R.drawable.outline_favorite_24) : getContext().getDrawable(R.drawable.outline_favorite_border_24))
        );

        actionAdapter.add(new Action(1, getString(R.string.bookmarked),
                isMarked ? getString(R.string.rem_bookmark) : getString(R.string.add_bookmark),
                isMarked ? getContext().getDrawable(R.drawable.outline_bookmark_24) : getContext().getDrawable(R.drawable.outline_bookmark_border_24)));


        for (int i = 0; i < Providers.values().length; i++) {
            actionAdapter.add(new Action(actionAdapter.size() + i, Providers.values()[i].getLanguage(), Providers.values()[i].name()));
        }

        upperRow.setActionsAdapter(actionAdapter);
        mAdapter.add(upperRow);

        if (mSelectedWatchable instanceof TVShow) {
            final TVShow mSelectedTVShow = (TVShow) mSelectedWatchable;

            final CardEpisodePresenter cardEpisodePresenter = new CardEpisodePresenter();

            for (int i = 0; i < mSelectedTVShow.getSeasons().length; i++) {
                final int value = mSelectedTVShow.getSeasons()[i];

                final ArrayObjectAdapter episodeListRow = new ArrayObjectAdapter(cardEpisodePresenter);
                final HeaderItem seasonHeader = new HeaderItem(i, "Season " + i);

                for (int j = 0; j < value; j++) {
                    episodeListRow.add(new Episode(j, i, mSelectedTVShow.getSeasonPoster(i)));
                }

                mAdapter.add(new ListRow(seasonHeader, episodeListRow));
            }
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

                default:
                    playback(action);
                    break;

            }
        });
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }

    private void playback(final Action action) {
        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

        if (mSelectedWatchable instanceof TVShow)
            Providers.valueOf(action.getLabel2().toString())
                    .getProvider()
                    .requestTVLink((TVShow) mSelectedWatchable, season, episode, s -> startPlayback(s, mDialog));
        else
            Providers.valueOf(action.getLabel2().toString())
                    .getProvider()
                    .requestMovieLink((Movie) mSelectedWatchable, s -> startPlayback(s, mDialog));
    }

    private void startPlayback(final Optional<ParcelableWatchableURLConnection> parcelableMovieURLConnection, final ProgressDialog progressDialog) {
        if (parcelableMovieURLConnection.isPresent()) {
            final Intent intent = new Intent(getActivity(), PlaybackActivity.class);
            intent.putExtra(DetailsActivity.WATCHABLE, mSelectedWatchable);
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