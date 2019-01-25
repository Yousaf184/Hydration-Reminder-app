package com.example.hydration_reminder_app;

import com.firebase.jobdispatcher.JobParameters;

import com.firebase.jobdispatcher.JobService;

public class ReminderJobService extends JobService {

    private Thread bgThread;

    @Override
    public boolean onStartJob(final JobParameters params) {

        bgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                NotificationUtil.createReminderNotification(ReminderJobService.this);
                jobFinished(params, false);
            }
        });

        bgThread.start();

        // return true means job is still working
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if(bgThread != null) {
            bgThread.interrupt();
        }

        // return true means job should be rescheduled once the requirements
        // of the job are met
        return true;
    }
}
