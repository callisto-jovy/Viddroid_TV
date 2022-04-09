package net.bplaced.abzzezz.videodroid.util.task;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskExecutor {

    /**
     * To ensure API level 30 compatibility
     */
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {
        callback.preExecute();
        executor.execute(() -> {
            try {
                final R result = callable.call();
                handler.post(() -> {
                    try {
                        callback.onComplete(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (final Exception e) {
                Log.e("Task Executor", "Error executing task: " + e.getLocalizedMessage());
                callback.exceptionCaught(e);
            }
        });
    }

    public interface Callback<R> {
        void onComplete(R result) throws Exception;

        void preExecute();

        void exceptionCaught(final Exception e);
    }
}
