package com.example.s345368m2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class NyVennFragment extends Fragment {
    private VennDataKilde dataKilde;
    private EditText innNavn, innTelefon;
    Button lagre, avbryt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.ny_venn_fragment,container, false);

        dataKilde = new VennDataKilde(getActivity());
        dataKilde.open();

        innNavn = view.findViewById(R.id.innNavn);
        innTelefon = view.findViewById(R.id.innTelefon);
        lagre = view.findViewById(R.id.lagreVenn);
        avbryt = view.findViewById(R.id.avbryt);

        lagre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vennNavn = innNavn.getText().toString();
                String vennTelefon = innTelefon.getText().toString();
                if (!vennNavn.isEmpty()) {
                    Venn venn = dataKilde.leggInnVenn(vennNavn, vennTelefon);
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
    }
}
