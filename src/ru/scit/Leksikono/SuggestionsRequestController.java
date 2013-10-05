package ru.scit.Leksikono;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: scit
 * Date: 9/24/13
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
abstract public class SuggestionsRequestController {
    private boolean suggestionsEnabled = true;
    private int threshold = 0;
    private int requestDelay = 1000;
    private Handler handler = null;
    private Runnable runnable = null;


    public SuggestionsRequestController(boolean suggestionsEnabled,
                                        int threshold, int requestDelay) {
        this.requestDelay = requestDelay;
        this.threshold = threshold;
        this.requestDelay = requestDelay;

        init();
    }

    public SuggestionsRequestController() {
        init();
    }

    public void init() {
        handler = new Handler();
    }

    public void attempt(int currentThreshold) {
        if (    (currentThreshold < threshold) ||
                (suggestionsEnabled == false)   ) {
            return;
        }

        handler.removeCallbacks(runnable);
        runnable = new Runnable() {
            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.
                SuggestionsRequestController.this.on();
            }
        };

        handler.postDelayed(runnable, requestDelay);

    }

    public void revokeRequests() {
        handler.removeCallbacks(runnable);
    }

    public void isRequestsEnabled(boolean enabled) {
        this.suggestionsEnabled = enabled;
    }

    public boolean isSuggestionsEnabled() {
        return this.suggestionsEnabled;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getRequestDelay() {
        return requestDelay;
    }

    public void setRequestDelay(int requestDelay) {
        this.requestDelay = requestDelay;
    }

    abstract public void on();
}
