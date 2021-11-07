package net.bplaced.abzzezz.videodroid.ui.search;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.SearchSupportFragment;
import androidx.leanback.widget.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import net.bplaced.abzzezz.videodroid.R;
import net.bplaced.abzzezz.videodroid.ui.details.DetailsActivity;
import net.bplaced.abzzezz.videodroid.ui.presenter.CardWatchablePresenter;
import net.bplaced.abzzezz.videodroid.util.task.TaskExecutor;
import net.bplaced.abzzezz.videodroid.util.task.tasks.TheMovieDBMovieSearchTask;
import net.bplaced.abzzezz.videodroid.util.task.tasks.TheMovieDBTVDetailsRequestTask;
import net.bplaced.abzzezz.videodroid.util.task.tasks.TheMovieDBTVSearchTask;
import net.bplaced.abzzezz.videodroid.util.watchable.Movie;
import net.bplaced.abzzezz.videodroid.util.watchable.TVShow;
import net.bplaced.abzzezz.videodroid.util.watchable.Watchable;
import org.json.JSONArray;

import java.util.Timer;
import java.util.TimerTask;

public class SearchFragment extends SearchSupportFragment implements SearchSupportFragment.SearchResultProvider {

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private ArrayObjectAdapter rowsAdapter;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    //   private SearchRunnable delayedLoad;

    private final HeaderItem movieHeader = new HeaderItem(0, "Movies");
    private final HeaderItem tvHeader = new HeaderItem(1, "TV-Shows");


    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        setupActionListeners();
        prepareBackgroundManager();

        setSearchResultProvider(this);
    }

    private void setupActionListeners() {
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
        setOnItemViewClickedListener(new ItemViewClickedListener());
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        mDefaultBackground = ContextCompat.getDrawable(getContext(), R.drawable.default_background);
        mMetrics = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return rowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        rowsAdapter.clear();
        if (!TextUtils.isEmpty(query))
            performSearch(query);
        return true;
    }

    private void performSearch(final String query) {
        final CardWatchablePresenter cardPresenter = new CardWatchablePresenter();
        final ArrayObjectAdapter WatchableArrayAdapter = new ArrayObjectAdapter(cardPresenter);
        final ArrayObjectAdapter tvArrayAdapter = new ArrayObjectAdapter(cardPresenter);

        new TheMovieDBMovieSearchTask(query).executeAsync(new TaskExecutor.Callback<JSONArray>() {
            @Override
            public void onComplete(final JSONArray result) throws Exception {
                for (int i = 0; i < result.length(); i++) {
                    WatchableArrayAdapter.add(new Movie(result.getJSONObject(i)));
                }
            }

            @Override
            public void preExecute() {

            }

            @Override
            public void exceptionCaught(Exception e) {

            }


        });

        new TheMovieDBTVSearchTask(query).executeAsync(new TaskExecutor.Callback<JSONArray>() {
            @Override
            public void onComplete(final JSONArray result) throws Exception {
                for (int i = 0; i < result.length(); i++) {
                    tvArrayAdapter.add(new TVShow(result.getJSONObject(i)));
                }
            }

            @Override
            public void preExecute() {

            }

            @Override
            public void exceptionCaught(Exception e) {

            }
        });


        rowsAdapter.add(new ListRow(movieHeader, WatchableArrayAdapter));
        rowsAdapter.add(new ListRow(tvHeader, tvArrayAdapter));
    }

    private void updateBackground(String uri) {
        final int width = mMetrics.widthPixels;
        final int height = mMetrics.heightPixels;

        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<Drawable>(width, height) {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                        mBackgroundManager.setDrawable(drawable);
                    }
                });
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Watchable) {
                mBackgroundUri = ((Watchable) item).getBackgroundImageUrl();
                startBackgroundTimer();
            }
        }
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            final Watchable watchable = (Watchable) item;

            if (watchable instanceof TVShow /*&& ((TVShow) watchable).isSeasonsNull() */) {
                new TheMovieDBTVDetailsRequestTask((TVShow) watchable).executeAsync(new TaskExecutor.Callback<TVShow>() {
                    @Override
                    public void onComplete(final TVShow result) {
                        transition(result, itemViewHolder);
                    }

                    @Override
                    public void preExecute() {
                    }

                    @Override
                    public void exceptionCaught(Exception e) {
                    }
                });
            } else transition(watchable, itemViewHolder);
        }

        private void transition(final Watchable watchable, final Presenter.ViewHolder itemViewHolder) {
            final Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra(DetailsActivity.WATCHABLE, watchable);

            final Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            getActivity(),
                            ((ImageCardView) itemViewHolder.view).getMainImageView(),
                            DetailsActivity.SHARED_ELEMENT_NAME)
                    .toBundle();
            getActivity().startActivity(intent, bundle);
        }
    }


    private class UpdateBackgroundTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(() -> updateBackground(mBackgroundUri));
        }
    }
}