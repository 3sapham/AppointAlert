package com.example.s345368m2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

public class AvtaleFragment extends Fragment {
    private AvtaleDataKilde dataKilde;
    private ArrayAdapter<Avtale> avtaleArrayAdapter;
    private List<Avtale> avtaler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.avtale_fragment,container, false);

        dataKilde = new AvtaleDataKilde(getActivity());
        dataKilde.open();

        ListView listView = view.findViewById(R.id.listView);

        avtaler = dataKilde.finnAlleAvtaler();

        avtaleArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.avtale_list_view, R.id.textView, avtaler);

        listView.setAdapter(avtaleArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Avtale valgtAvtale = avtaleArrayAdapter.getItem(position);
                loadRedigerAvtaleFragment(valgtAvtale.getId());
            }
        });

        return view;
    }

    private void loadRedigerAvtaleFragment(long avtaleId) {
        RedigerAvtaleFragment redigerAvtaleFragment = new RedigerAvtaleFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("avtaleId", avtaleId);
        redigerAvtaleFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, redigerAvtaleFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        dataKilde.open();
        avtaler = dataKilde.finnAlleAvtaler();
        super.onResume();
    }

    @Override
    public void onPause() {
        dataKilde.close();
        super.onPause();
    }
}
