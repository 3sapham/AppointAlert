package com.example.s345368m2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class RedigerVennFragment extends Fragment {
    private VennDataKilde dataKilde;
    private EditText innNavn, innTelefon;
    Button oppdater, avbryt, slett;
    private long vennId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.rediger_venn_fragment,container, false);

        dataKilde = new VennDataKilde(getActivity());
        dataKilde.open();

        innNavn = view.findViewById(R.id.nyNavn);
        innTelefon = view.findViewById(R.id.nyTelefon);
        oppdater = view.findViewById(R.id.oppdaterVenn);
        avbryt = view.findViewById(R.id.avbrytVenn);
        slett = view.findViewById(R.id.slettVenn);

        Bundle args = getArguments();
        if (args != null) {
            vennId = args.getLong("vennId");
            Venn valgtVenn = dataKilde.finnVennMedId(vennId);
            if (valgtVenn != null) {
                innNavn.setText(valgtVenn.getNavn());
                innTelefon.setText(valgtVenn.getTelefon());
            }
        }

        oppdater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vennNavn = innNavn.getText().toString();
                String vennTelefon = innTelefon.getText().toString();
                if (!vennNavn.isEmpty()) {
                    dataKilde.oppdaterVenn(vennId,vennNavn,vennTelefon);
                    Toast.makeText(getActivity(), "Venn oppdatert", Toast.LENGTH_SHORT).show();
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

        slett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataKilde.slettVenn(vennId);
                Toast.makeText(getActivity(), "Venn slettet", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataKilde.close();
    }
}
