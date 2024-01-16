package com.example.s345368m2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    String CHANNEL_ID = "MinKanal";
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        BroadcastReceiver minBroadcastReceiver = new MinBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.example.s345368m2.MITTSIGNAL");
        filter.addAction("com.example.s345368m2.MITTSIGNAL");
        this.registerReceiver(minBroadcastReceiver, filter);

        loadFragment(new AvtaleFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.avtaler) {
                loadFragment(new AvtaleFragment());
                return true;
            } else if (itemId == R.id.venner) {
                loadFragment(new VennFragment());
                return true;
            } else if (itemId == R.id.preferanser) {
                loadFragment(new SettingsFragment());
                return true;
            }
            return false;
        });

        fab = findViewById(R.id.fab);
        FragmentManager fm = getSupportFragmentManager();

        fm.addOnBackStackChangedListener(() -> {
            Fragment currentFragment = fm.findFragmentById(R.id.frameLayout);
            if (currentFragment instanceof VennFragment) {
                fab.setOnClickListener(view -> loadFragment(new NyVennFragment()));
            } else {
                fab.setOnClickListener(view -> loadFragment(new NyAvtaleFragment()));
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void createNotificationChannel() {
        CharSequence name = "Min Kanal";
        String description = "Min Beskrivelse";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SEND_SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Gi SMS tillatelse for å sende SMS.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}