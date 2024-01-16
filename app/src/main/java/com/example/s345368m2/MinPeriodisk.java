package com.example.s345368m2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MinPeriodisk extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String tidspunkt = sharedPref.getString("sms_tid", "06:00");
        long interval = AlarmManager.INTERVAL_DAY;

        java.util.Calendar tid = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.getDefault());
            Date time = sdf.parse(tidspunkt);
            tid.setTime(time);
        } catch( ParseException e){
            e.printStackTrace();
        }

        Intent i = new Intent(this, MinSendService.class);
        PendingIntent pIntent = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, tid.getTimeInMillis(), interval, pIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

