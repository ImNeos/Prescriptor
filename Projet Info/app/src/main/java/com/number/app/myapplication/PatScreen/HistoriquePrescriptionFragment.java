package com.number.app.myapplication.PatScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.number.app.myapplication.DB.DB_Data;
import com.number.app.myapplication.R;
import com.number.app.myapplication.Util.StaticValues;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HistoriquePrescriptionFragment extends Fragment {



     int compteur = 0;

    public HistoriquePrescriptionFragment() {
        // Required empty public constructor
    }

    ArrayAdapter<String> itemsAdapter;
    List<String> list_histo = new ArrayList<>();
    ListView listView;
    View view;
    DatabaseReference db_ref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_historique_prescription, container, false);
        Init();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OpenDialog(list_histo.get(i));
            }
        });
        return view;
    }

    private void OpenDialog(final String s)
    {
        final List<String> list_med = new ArrayList<>();
        final List<String> list_name_med = new ArrayList<>();

        db_ref.child(StaticValues.pat_root).child(DB_Data.getInstance(getContext()).getNote(StaticValues.reg_nat)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot d: dataSnapshot.getChildren())
                    {
                        list_med.add(d.getValue().toString());
                    }
                    for (int i = 0; i< list_med.size();i++)
                    {
                        db_ref.child(StaticValues.users_root).child(list_med.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if (dataSnapshot.exists())
                                {
                                    list_name_med.add(dataSnapshot.child(StaticValues.FirstName).getValue().toString() + " " + dataSnapshot.child(StaticValues.LastName).getValue().toString());
                                    compteur++;
                                    if (compteur == list_med.size())
                                    {
                                        compteur =0;
                                        new LovelyChoiceDialog(getContext())
                                                .setTopColorRes(R.color.colorPrimary)
                                                .setTitle("Prescription : " + s )
                                                .setMessage("Choose the med you want to ask")
                                                .setItems(list_name_med, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                                                    @Override
                                                    public void onItemSelected(int position, String item) {
                                                        String key = db_ref.push().getKey().toString();
                                                        HashMap<String, String> hashMap = new HashMap();
                                                        hashMap.put(StaticValues.name, s);
                                                        hashMap.put(StaticValues.reg_nat,DB_Data.getInstance(getContext()).getNote(StaticValues.reg_nat));
                                                        db_ref.child(StaticValues.demand_root).child(list_med.get(position)).child(key).setValue(hashMap);
                                                        Toast.makeText(getContext(), "Done ! ", Toast.LENGTH_SHORT).show();

                                                    }
                                                })
                                                .show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Init()
    {
        db_ref= FirebaseDatabase.getInstance().getReference();
        listView = (ListView) view.findViewById(R.id.list_view);
        listView = view.findViewById(R.id.list_view);
        itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_histo);
        listView.setAdapter(itemsAdapter);
        FillInTheList();
    }

    private void FillInTheList()
    {
        list_histo.clear();
        db_ref.child(StaticValues.histo_root).child(DB_Data.getInstance(getContext()).getNote(StaticValues.reg_nat)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot d: dataSnapshot.getChildren())
                {
                    list_histo.add(d.getValue().toString());
                    itemsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}