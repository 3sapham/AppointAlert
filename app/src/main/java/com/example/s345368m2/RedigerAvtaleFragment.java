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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RedigerAvtaleFragment extends Fragment {
    private AvtaleDataKilde dataKilde;
    private VennDataKilde vennDataKilde;
    private ArrayAdapter<Venn> vennArrayAdapter;
    private List<Venn> venner;
    private EditText nyTittel, nyTreffsted;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private String valgtTidspunkt = "";
    private String valgtDato = "";
    Spinner vennerDropdown;
    private Button lagre, avbryt, slett;
    private long avtaleId;
    private int valgtVennId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rediger_avtale_fragment, container, false);

        dataKilde = new AvtaleDataKilde(getActivity());
        dataKilde.open();

        vennDataKilde = new VennDataKilde(getActivity());
        vennDataKilde.open();

        nyTittel = view.findViewById(R.id.nyTittel);
        datePicker = view.findViewById(R.id.nyDatePicker);
        timePicker = view.findViewById(R.id.nyTimePicker);
        nyTreffsted = view.findViewById(R.id.nyTreffsted);
        vennerDropdown = view.findViewById(R.id.nyVennerDropdown);
        lagre = view.findViewById(R.id.lagre);
        avbryt = view.findViewById(R.id.avbryt);
        slett = view.findViewById(R.id.slett);

        Bundle args = getArguments();
        if (args != null) {
            avtaleId = args.getLong("avtaleId");
        }

        Avtale valgtAvtale = dataKilde.finnAvtaleMedId(avtaleId);
        valgtDato = valgtAvtale.getDato();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = sdf.parse(valgtDato);

            cal.setTime(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                valgtDato = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);
            }
        });

        valgtTidspunkt = valgtAvtale.getKlokkeslett();
        SimpleDateFormat sdft = new SimpleDateFormat("HH:mm",Locale.getDefault());

        try {
            Date time = sdft.parse(valgtTidspunkt);
            cal.setTime(time);

        } catch( ParseException e){
            e.printStackTrace();
        }
        timePicker.setHour(cal.getTime().getHours());
        timePicker.setMinute(cal.getTime().getMinutes());
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                valgtTidspunkt = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            }
        });

        nyTittel.setText(valgtAvtale.getTittel());

        nyTreffsted.setText(valgtAvtale.getTreffsted());

        valgtVennId = (int) valgtAvtale.getVenn().getId();
        venner = vennDataKilde.finnAlleVenner();
        for (int i = 0; i < venner.size(); i++) {
            if (venner.get(i).getId() == valgtAvtale.getVenn().getId()) {
                valgtVennId = i;
                break;
            }
        }
        vennArrayAdapter = new ArrayAdapter<Venn>(getActivity(), android.R.layout.simple_spinner_item, venner);
        vennArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vennerDropdown.setAdapter(vennArrayAdapter);
        vennerDropdown.setSelection(valgtVennId);
        vennerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Venn valgt = vennArrayAdapter.getItem(i);
                valgtVennId = (int) valgt.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d( "String","Error dropdown");
            }

        });

        lagre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String avtaleTittel = nyTittel.getText().toString();
                String avtaleDato = valgtDato;
                String avtaleKlokkeslett = valgtTidspunkt;
                String avtaleTreffsted = nyTreffsted.getText().toString();
                long vennId = valgtVennId;

                dataKilde.oppdaterAvtale(avtaleId, avtaleTittel,avtaleDato,avtaleKlokkeslett,avtaleTreffsted,vennId);

                Toast.makeText(getActivity(), "Avtale oppdatert", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        avbryt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        slett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataKilde.slettAvtale(avtaleId);
                Toast.makeText(getActivity(), "Avtale slettet", Toast.LENGTH_SHORT).show();
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
