package com.number.app.myapplication.MedScreen;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.number.app.myapplication.DB.DB_Data;
import com.number.app.myapplication.R;
import com.number.app.myapplication.Util.StaticValues;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PatFragment extends Fragment {

    FloatingActionButton fab;
    View view;
    Context context;
    DatabaseReference db_ref;
    ArrayAdapter<String> itemsAdapter;
    List <String> list_of_pat = new ArrayList<>();
    ListView listView;

    public PatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db_ref = FirebaseDatabase.getInstance().getReference();
        context = getContext();
        view= inflater.inflate(R.layout.fragment_pat, container, false);
        InitField();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPatient();
            }
        });
        return view;
    }

    private void AddPatient()
    {
        new LovelyTextInputDialog(context)
                .setTopColorRes(R.color.colorAccent)
                .setTitle("Ajouter un patient")
                .setMessage("Entrer le numéro de registre national du patient")
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(final String text) {
                        db_ref.child(StaticValues.users_root).child(text).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists())
                                {
                                    Log.i("PATIENT", "OK EXISTE!");
                                    AddNewPatients(text);
                                }
                                else
                                {
                                    Toast.makeText(context, "Cette personne n'existe pas!", Toast.LENGTH_SHORT).show();
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

    private void AddNewPatients(String text) {
        String key= FirebaseDatabase.getInstance().getReference().push().getKey();
        db_ref.child(StaticValues.med_root).child(DB_Data.getInstance(context).getNote(StaticValues.reg_nat)).child(key).setValue(text).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FillInTheList();
                Toast.makeText(context, "Ajouté ! ", Toast.LENGTH_SHORT).show();
            }
        });
        db_ref.child(StaticValues.pat_root).child(text).child(key).setValue(DB_Data.getInstance(context).getNote(StaticValues.reg_nat));


    }

    private void InitField() {
       
        FillInTheList();
        fab = view.findViewById(R.id.fab_add);
        listView = view.findViewById(R.id.list_view);
        itemsAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list_of_pat);
        listView.setAdapter(itemsAdapter);
    }

    private void FillInTheList()
    {
        list_of_pat.clear();
        db_ref.child(StaticValues.med_root).child(DB_Data.getInstance(context).getNote(StaticValues.reg_nat)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot d: dataSnapshot.getChildren())
                {
                    db_ref.child(StaticValues.users_root).child(d.getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                list_of_pat.add(dataSnapshot.child(StaticValues.FirstName).getValue().toString() + " "+ dataSnapshot.child(StaticValues.LastName).getValue().toString());
                                itemsAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}