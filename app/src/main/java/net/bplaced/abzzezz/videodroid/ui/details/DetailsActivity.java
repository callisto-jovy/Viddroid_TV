package net.bplaced.abzzezz.videodroid.ui.details;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import net.bplaced.abzzezz.videodroid.R;

/*
 * Details activity class that loads LeanbackDetailsFragment class
 */
public class DetailsActivity extends FragmentActivity {
    public static final String SHARED_ELEMENT_NAME = "hero";
    public static final String WATCHABLE = "watchable";
    public static final String WATCHABLE_URL = "watchable_url_connection";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment, new VideoDetailsFragment())
                    .commitNow();
        }
    }

}