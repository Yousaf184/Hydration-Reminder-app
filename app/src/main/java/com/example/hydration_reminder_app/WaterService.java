package com.example.hydration_reminder_app;

import android.app.IntentService;
import android.content.Intent;

public class WaterService extends IntentService {

    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    public static final String ACTION_RESET_WATER_COUNT = "reset-water-count";
    public static final String ACTION_DISMISS_NOTIFICATION = "clear-all-notifications";

    public WaterService() {
        super("WaterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if(action != null && action.equals(ACTION_INCREMENT_WATER_COUNT)) {
            incrementWaterCount();
        }
        else if(action != null && action.equals(ACTION_RESET_WATER_COUNT)){
            resetWaterCount();
        }
        else if(action != null && action.equals(ACTION_DISMISS_NOTIFICATION)){
            NotificationUtil.clearAllNotifications(this);
        }
    }

    private void incrementWaterCount() {
        SharedPreferenceUtil.incrementWaterCount(this);

        // useful when an action to increment water count is clicked
        // on notification
        NotificationUtil.clearAllNotifications(this);
    }

    private void resetWaterCount() {
        SharedPreferenceUtil.resetWaterCount(this);
    }
}
