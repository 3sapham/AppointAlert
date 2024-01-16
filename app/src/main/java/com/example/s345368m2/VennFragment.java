package com.example.s345368m2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VennFragment extends Fragment {
    private VennDataKilde dataKilde;
    private ArrayAdapter<Venn> vennArrayAdapter;
    private List<Venn> venner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.venn_fragment,container, false);

        dataKilde = new VennDataKilde(getActivity());
        dataKilde.open();

        ListView vennListView = view.findViewById(R.id.listViewVenner);

        venner = dataKilde.finnAlleVenner();

        vennArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.avtale_list_view, R.id.textView, venner);

        vennListView.setAdapter(vennArrayAdapter);

        vennListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Venn valgtVenn = vennArrayAdapter.getItem(position);
                // Åpne RedigerVennFragment og send vennens ID for redigering
                loadRedigerVennFragment(valgtVenn.getId());
            }
        });

        return view;
    }

    private void loadRedigerVennFragment(long vennId) {
        RedigerVennFragment redigerVennFragment = new RedigerVennFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("vennId", vennId);
        redigerVennFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, redigerVennFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        dataKilde.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        dataKilde.close();
        super.onPause();
    }
}
