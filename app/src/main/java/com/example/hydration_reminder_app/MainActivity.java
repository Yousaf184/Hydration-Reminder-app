package com.example.hydration_reminder_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
                         implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView waterCount;
    private Button resetWaterCountBtn;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waterCount = findViewById(R.id.water_count);
        resetWaterCountBtn = findViewById(R.id.reset_water_count);

        int count = SharedPreferenceUtil.getWaterCountFromSharedPrefs(this);

        if(count == -1) {
            disableResetButton();
        }

        setWaterCount(SharedPreferenceUtil.getWaterCountFromSharedPrefs(this));

        RemindersUtil.scheduleReminders(this);
    }

    private void disableResetButton() {
        resetWaterCountBtn.setBackgroundColor(Color.LTGRAY);
        resetWaterCountBtn.setEnabled(false);
    }

    private void enableResetButton() {
        resetWaterCountBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        resetWaterCountBtn.setEnabled(true);
    }

    public void incrementWaterCount(View v) {
        Intent waterService = new Intent(this, WaterService.class);
        waterService.setAction(WaterService.ACTION_INCREMENT_WATER_COUNT);
        startService(waterService);

        if(toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(this, "Congrats, you are hydrated now!!", Toast.LENGTH_LONG);
        toast.show();

        enableResetButton();
    }

    private void setWaterCount(int waterCounter) {
        if(waterCounter == -1) {
            waterCount.setText("0");
        } else {
            waterCount.setText(String.valueOf(waterCounter));
        }
    }

    public void resetWaterCount(View v) {
        Intent waterService = new Intent(this, WaterService.class);
        waterService.setAction(WaterService.ACTION_RESET_WATER_COUNT);
        startService(waterService);

        if(toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(this, "Water count reset", Toast.LENGTH_LONG);
        toast.show();

        disableResetButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                         .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        int count = sharedPreferences.getInt(key, -1);
        setWaterCount(count);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                         .unregisterOnSharedPreferenceChangeListener(this);
    }
}
