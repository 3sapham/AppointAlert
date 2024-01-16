package com.example.s345368m2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NyAvtaleFragment extends Fragment {
    private AvtaleDataKilde dataKilde;
    private VennDataKilde vennDataKilde;
    private ArrayAdapter<Venn> vennArrayAdapter;
    private List<Venn> venner;
    private EditText innTittel, innTreffsted;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private String valgtTidspunkt = "";
    private String valgtDato = "";
    Spinner vennerDropdown;
    Button leggtil, avbryt;
    long valgtVennId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.ny_avtale_fragment,container, false);
        dataKilde = new AvtaleDataKilde(getActivity());
        dataKilde.open();

        vennDataKilde = new VennDataKilde(getActivity());
        vennDataKilde.open();

        innTittel = view.findViewById(R.id.innTittel);
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        innTreffsted = view.findViewById(R.id.innTreffsted);
        vennerDropdown = view.findViewById(R.id.vennerDropdown);
        leggtil = view.findViewById(R.id.leggtil);
        avbryt = view.findViewById(R.id.avbryt);

        java.util.Calendar cal = Calendar.getInstance();
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                valgtDato = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);
            }
        });

        timePicker.setHour(cal.getTime().getHours());
        timePicker.setMinute(cal.getTime().getMinutes());
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                valgtTidspunkt = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            }
        });

        venner = vennDataKilde.finnAlleVenner();
        vennArrayAdapter = new ArrayAdapter<Venn>(getActivity(), android.R.layout.simple_spinner_item, venner);
        vennArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vennerDropdown.setAdapter(vennArrayAdapter);
        vennerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Venn valgt = vennArrayAdapter.getItem(i);
                valgtVennId = valgt.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d( "String","Error dropdown");
            }

        });

        leggtil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String avtaleTittel = innTittel.getText().toString();
                String avtaleDato = valgtDato;
                String avtaleKlokkeslett = valgtTidspunkt;
                String avtaleTreffsted = innTreffsted.getText().toString();
                long vennId = valgtVennId;
                if (!avtaleTittel.isEmpty()) {
                    Avtale avtale = dataKilde.leggInnAvtale(avtaleTittel, avtaleDato, avtaleKlokkeslett, avtaleTreffsted, vennId);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        avbryt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataKilde.close();
        vennDataKilde.close();
    }
}
