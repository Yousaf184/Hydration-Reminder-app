package com.example.hydration_reminder_app;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class RemindersUtil {

    private static final String JOB_TAG = "water-reminder-job-tag";
    private static final int REMINDER_INTERVAL_SECONDS = 800;
    private static final int REMINDER_INTERVAL_LEEWAY_SECONDS = 900; // 15 minutes

    public static void scheduleReminders(Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job job = dispatcher.newJobBuilder()
                            .setService(ReminderJobService.class)
                            .setTag(JOB_TAG)
                            .setConstraints(Constraint.ON_ANY_NETWORK)
                            .setLifetime(Lifetime.FOREVER)
                            .setRecurring(true)
                            .setReplaceCurrent(true)
                            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                            .setTrigger(
                                    Trigger.executionWindow(
                                            REMINDER_INTERVAL_SECONDS,
                                            REMINDER_INTERVAL_LEEWAY_SECONDS
                                    )
                            )
                            .build();

        dispatcher.mustSchedule(job);
    }
}
