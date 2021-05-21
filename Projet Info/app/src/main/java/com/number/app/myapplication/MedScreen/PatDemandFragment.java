package com.number.app.myapplication.MedScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.number.app.myapplication.DB.DB_Data;
import com.number.app.myapplication.R;
import com.number.app.myapplication.Util.StaticValues;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PatDemandFragment extends Fragment {


    DatabaseReference databaseReference;
    FloatingActionButton fab;
    View view;

    ArrayAdapter<String> itemsAdapter;
    List<String> list_aff = new ArrayList<>();
    List<list_model> list_pat = new ArrayList<>();

    DatabaseReference db_ref;
    ListView listView;


    public PatDemandFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void OpenDialog()
    {
        final List<String> list_med = new ArrayList<>();
        databaseReference.child(StaticValues.medoc_root).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()) {
                    list_med.add(d.getKey()); // not very optimized but it's a long way to create a research bar.
                }
                new LovelyChoiceDialog(getContext())
                        .setTopColorRes(R.color.colorAccent)
                        .setTitle("Prescription")
                        .setItemsMultiChoice(list_med, new LovelyChoiceDialog.OnItemsSelectedListener<String>() {
                            @Override
                            public void onItemsSelected(List<Integer> positions, List<String> items) {
                                MakeAPrescription(positions, items);
                            }
                        })
                        .setConfirmButtonText("Ok")
                        .show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void MakeAPrescription(final List<Integer> positions, final List<String> items)
    {
        new LovelyTextInputDialog(getContext())
                .setTopColorRes(R.color.colorAccent)
                .setTitle("Prescription")
                .setMessage("Entrer le numéro de registre national du patient auquel vous devez prescrire")
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(final String text) {

                        databaseReference.child(StaticValues.users_root).child(text).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) //patient exist
                                {
                                    for (int i =0; i <positions.size();i++)
                                    {
                                        databaseReference.child(StaticValues.PrescriptionRoot).child(text).push().setValue(items.get(i));
                                    }
                                    Toast.makeText(getContext(), "OK!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .show();

    }

    private void InitField() {
        fab = view.findViewById(R.id.fab_add);
        listView = view.findViewById(R.id.list_view);
        db_ref= FirebaseDatabase.getInstance().getReference();
        listView = (ListView) view.findViewById(R.id.list_view);
        listView = view.findViewById(R.id.list_view);
        itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_aff);
        listView.setAdapter(itemsAdapter);
        FillInTheList();


    }

    private void FillInTheList()
    {
        db_ref.child(StaticValues.demand_root).child(DB_Data.getInstance(getContext()).getNote(StaticValues.reg_nat)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    list_aff.clear();
                    list_pat.clear();
                    for (DataSnapshot d : dataSnapshot.getChildren())
                    {
                        FillList(d.getKey().toString(), d.child(StaticValues.reg_nat).getValue().toString(), d.child(StaticValues.name).getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FillList(final String key, final String id, final String medoc_name)
    {
        db_ref.child(StaticValues.users_root).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    list_model list_mod = new list_model(dataSnapshot.child(StaticValues.LastName).getValue().toString(), dataSnapshot.child(StaticValues.FirstName).getValue().toString(),
                            id, key, medoc_name);
                    list_aff.add(medoc_name + " : " + dataSnapshot.child(StaticValues.FirstName).getValue().toString() + " " + dataSnapshot.child(StaticValues.LastName).getValue().toString());
                    list_pat.add(list_mod);

                    Log.i("Test", list_mod.getFirst_name() + list_mod.getId() + list_mod.getReg());
                    itemsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_pat_demand, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        InitField();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialog();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l)
            {
                new LovelyStandardDialog(getContext(), LovelyStandardDialog.ButtonLayout.VERTICAL)
                        .setTopColorRes(R.color.colorPrimary)
                        .setTitle(list_pat.get(i).getMedoc_name())
                        .setMessage("Accept the prescription ? ")
                        .setPositiveButton("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                //Send Prescription
                                db_ref.child(StaticValues.PrescriptionRoot).child(list_pat.get(i).getReg()).push().setValue(list_pat.get(i).getMedoc_name());
                                db_ref.child(StaticValues.demand_root).child(DB_Data.getInstance(getContext()).getNote(StaticValues.reg_nat)).child(list_pat.get(i).getId()).removeValue();
                                list_aff.remove(i);
                                list_pat.remove(i);
                                itemsAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Accepted !", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No, delete", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                db_ref.child(StaticValues.demand_root).child(DB_Data.getInstance(getContext()).getNote(StaticValues.reg_nat)).child(list_pat.get(i).getId()).removeValue();
                                list_aff.remove(i);
                                list_pat.remove(i);
                                itemsAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Deleted ! ", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
        return view;
    }
}