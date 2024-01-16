package com.example.s345368m2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MinBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "I broadcast", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, MinPeriodisk.class);
        context.startService(i);
    }
}

