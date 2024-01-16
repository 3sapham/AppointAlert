package com.example.s345368m2;

import static android.content.Context.ALARM_SERVICE;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferanser, rootKey);

        Preference varsling = findPreference("sms_varsling");
        Preference tidspunkt = findPreference("sms_tid");
        Preference melding = findPreference("sms_melding");

        varsling.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean aktivert = (boolean) newValue;

                if (aktivert) {
                    Toast.makeText(getContext(), "Varsel aktivert", Toast.LENGTH_SHORT).show();
                    tidspunkt.setEnabled(true);
                    melding.setEnabled(true);
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
                        return false;
                    }
                    Intent intent = new Intent();
                    intent.setAction("com.example.s345368m2.MITTSIGNAL");
                    getContext().sendBroadcast(intent);
                } else {
                    Toast.makeText(getContext(), "Varsel deaktivert", Toast.LENGTH_SHORT).show();
                    tidspunkt.setEnabled(false);
                    melding.setEnabled(false);
                    Intent i = new Intent(getContext(), MinSendService.class);
                    PendingIntent pintent = PendingIntent.getBroadcast(getContext(), 0, i, 0);
                    AlarmManager alarm = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
                    if (alarm != null) {
                        alarm.cancel(pintent);
                    }
                }
                return true;
            }
        });

        tidspunkt.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String egenMelding = (String) newValue;

                if (gyldigFormat(egenMelding)) {
                    Toast.makeText(getContext(), "Tidspunkt satt til "+egenMelding, Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("sms_tid", egenMelding);
                    editor.apply();

                    return true;
                } else {
                    Toast.makeText(getContext(), "Tiden må være på formatet 00:00.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        });
    }

    private boolean gyldigFormat(String value) {
        return value.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
    }
}
