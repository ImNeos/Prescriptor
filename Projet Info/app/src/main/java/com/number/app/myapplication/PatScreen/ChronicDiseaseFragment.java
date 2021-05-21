package com.number.app.myapplication.PatScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.number.app.myapplication.DB.DB_Data;
import com.number.app.myapplication.R;
import com.number.app.myapplication.Util.StaticValues;

import java.util.ArrayList;
import java.util.List;


public class ChronicDiseaseFragment extends Fragment {


    public ChronicDiseaseFragment() {
        // Required empty public constructor
    }


    ArrayAdapter<String> itemsAdapter;
    List<String> list_disease = new ArrayList<>();
    ListView listView;
    View view;
    DatabaseReference db_ref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        view = inflater.inflate(R.layout.fragment_chronic_disease, container, false);
        Init();
        return view;
    }

    private void Init()
    {
        db_ref= FirebaseDatabase.getInstance().getReference();
        listView = (ListView) view.findViewById(R.id.list_view);
        listView = view.findViewById(R.id.list_view);
        itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_disease);
        listView.setAdapter(itemsAdapter);

        FillInTheList();
    }

    private void FillInTheList()
    {
        list_disease.clear();
        db_ref.child(StaticValues.users_root).child(DB_Data.getInstance(getContext()).getNote(StaticValues.reg_nat)).child(StaticValues.chronic_di).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot d: dataSnapshot.getChildren())
                    {
                        list_disease.add(d.getValue().toString());
                        itemsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}