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


public class PrescriptionReceivedFragment extends Fragment {



    ArrayAdapter<String> itemsAdapter;
    List<String> list_prescri = new ArrayList<>();
    ListView listView;
    View view;
    DatabaseReference db_ref;

    public PrescriptionReceivedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        

        view =  inflater.inflate(R.layout.fragment_prescription_received, container, false);
        Init();

        return view;
    }

    private void Init() {
        
        listView = (ListView) view.findViewById(R.id.list_view);
        listView = view.findViewById(R.id.list_view);
        itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_prescri);
        listView.setAdapter(itemsAdapter);
        db_ref = FirebaseDatabase.getInstance().getReference();
        FillInTheList();
    }

    private void FillInTheList()
    {
        list_prescri.clear();
       db_ref.child(StaticValues.PrescriptionRoot).child(DB_Data.getInstance(getContext()).getNote(StaticValues.reg_nat)).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               list_prescri.clear();
               for (DataSnapshot d: dataSnapshot.getChildren())
               {
                   if (d.exists())
                   {
                       list_prescri.add(d.getValue().toString());
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