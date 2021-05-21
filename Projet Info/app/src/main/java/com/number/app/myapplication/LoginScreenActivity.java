package com.number.app.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.number.app.myapplication.DB.DB_Data;
import com.number.app.myapplication.MedScreen.MedScreenFirstActivity;
import com.number.app.myapplication.PatScreen.PatFirstScreenActivity;
import com.number.app.myapplication.Util.NagivationClass;
import com.number.app.myapplication.Util.StaticValues;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

public class LoginScreenActivity extends AppCompatActivity {

    EditText et_reg, et_mdp;
    CheckBox check_pat;
    Button btn_connect;
    String reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        InitField();

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckConnection();
            }
        });

    }

    private void InitField() {

        btn_connect = findViewById(R.id.btn_connect);
        et_mdp = findViewById(R.id.et_mdp);
        et_reg = findViewById(R.id.et_reg);
        check_pat = findViewById(R.id.check_doc_pat);
    }
    private void CheckConnection()
    {
        //If connected already return true
        //else

         reg = et_reg.getText().toString();
        final String mdp = et_mdp.getText().toString();

        FirebaseDatabase.getInstance().getReference().child(StaticValues.users_root).child(reg).child(StaticValues.password_root).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.getValue().toString().equals(mdp))
                    {
                        Log.i("MDP", "MDP OK");
                        DB_Data.getInstance(LoginScreenActivity.this).addElementTodB(StaticValues.reg_nat, reg);
                        DB_Data.getInstance(LoginScreenActivity.this).addElementTodB(StaticValues.password_root, mdp);
                        CheckIfMed();
                    }
                }
                else
                {
                    Log.i("MDP", "Error");

                    //Error
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckIfMed() {
        FirebaseDatabase.getInstance().getReference().child(StaticValues.users_root).child(reg).child(StaticValues.users_med_root).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue().toString().equals("1"))
                {
                    new LovelyStandardDialog(LoginScreenActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                            .setTopColorRes(R.color.colorPrimary)
                            .setButtonsColorRes(R.color.colorPrimary)
                            .setTitle("Medecin")
                            .setMessage("Voulez-vous accéder à l'interface médecin ou patient?")
                            .setPositiveButton("Médecin", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    NagivationClass.SendUserToOtherActivity(LoginScreenActivity.this, MedScreenFirstActivity.class, LoginScreenActivity.this);
                                }
                            })
                            .setNegativeButton("Patient", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    NagivationClass.SendUserToOtherActivity(LoginScreenActivity.this, PatFirstScreenActivity.class, LoginScreenActivity.this);

                                }
                            })
                            .show();
                }
                else
                {
                    NagivationClass.SendUserToOtherActivity(LoginScreenActivity.this, PatFirstScreenActivity.class, LoginScreenActivity.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}