package net.bplaced.abzzezz.videodroid.ui.details;

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;
import net.bplaced.abzzezz.videodroid.util.watchable.Watchable;

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        final Watchable watchable = (Watchable) item;

        if (watchable != null) {
            viewHolder.getTitle().setText(watchable.getTitle());
            // viewHolder.getSubtitle().setText(movie.getStudio());
            viewHolder.getBody().setText(watchable.getDescription());
        }
    }
}