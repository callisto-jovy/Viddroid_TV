package net.bplaced.abzzezz.videodroid.ui.presenter;

import android.util.Log;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;
import com.bumptech.glide.Glide;
import net.bplaced.abzzezz.videodroid.R;
import net.bplaced.abzzezz.videodroid.util.watchable.Episode;

public class CardEpisodePresenter extends Presenter {

    private static final String TAG = "CardEpisodePresenter";

    private static final int CARD_WIDTH = 300;
    private static final int CARD_HEIGHT = 450;

    private int sSelectedBackgroundColor;
    private int sDefaultBackgroundColor;

    private void updateCardBackgroundColor(final ImageCardView view, final boolean selected) {
        final int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        this.sDefaultBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.default_background);
        this.sSelectedBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.selected_background);
        /*
         * This template uses a default image in res/drawable, but the general case for Android TV
         * will require your resources in xhdpi. For more information, see
         * https://developer.android.com/training/tv/start/layouts.html#density-resources
         */
        //  Drawable mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.movie);

        final ImageCardView cardView = new ImageCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new Presenter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        final Episode episode = (Episode) item;

        final ImageCardView cardView = (ImageCardView) viewHolder.view;

        Log.d(TAG, "onBindViewHolder");

        if (episode.getSeasonPoster() != null) {
            cardView.setTitleText("Episode " + episode.getIndex());
            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);

            Glide.with(viewHolder.view.getContext())
                    .load(episode.getSeasonPoster())
                    .centerCrop()
                    .error("https://www.themoviedb.org/assets/2/apple-touch-icon-cfba7699efe7a742de25c28e08c38525f19381d31087c69e89d6bcb8e3c0ddfa.png")
                    .into(cardView.getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        final ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
