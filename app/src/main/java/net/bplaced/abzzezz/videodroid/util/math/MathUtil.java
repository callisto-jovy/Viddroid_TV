package net.bplaced.abzzezz.videodroid.util.math;

import android.content.Context;

public class MathUtil {

    public static int convertDpToPixel(Context context, int dp) {
        final float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
