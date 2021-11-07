package net.bplaced.abzzezz.videodroid.ui.search;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import net.bplaced.abzzezz.videodroid.R;

public class SearchActivity extends FragmentActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.search_fragment, new SearchFragment())
                    .commitNow();
        }
    }
}
