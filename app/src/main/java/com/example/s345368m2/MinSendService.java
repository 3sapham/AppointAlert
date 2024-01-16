package com.example.s345368m2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import java.util.Date;
import java.util.List;

public class MinSendService extends Service {
    private AvtaleDataKilde avtaleDataKilde;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "I MinSendService", Toast.LENGTH_SHORT).show();

        avtaleDataKilde = new AvtaleDataKilde(this);
        avtaleDataKilde.open();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String pref_melding = sharedPref.getString("sms_melding", "");
        String melding = pref_melding;

        List<Avtale> avtaler = avtaleDataKilde.finnAvtalerForIdag();
        for (Avtale avtale : avtaler) {
            int notifikasjonId = (int) avtale.getId();
            sendSMS(avtale.getVenn(), melding);
            sendNotifikasjon(avtale, melding, notifikasjonId);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void sendSMS(Venn venn, String melding) {
        String telefon = venn.getTelefon();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(telefon, null, melding, null, null);
    }

    private void sendNotifikasjon(Avtale avtale, String melding, int notifikasjonId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
        Notification notifikasjon = new NotificationCompat.Builder(this, "MinKanal")
                .setContentTitle(avtale.toString())
                .setContentText(melding)
                .setSmallIcon(R.drawable.baseline_alarm_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pIntent).build();

        notifikasjon.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(notifikasjonId, notifikasjon);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
