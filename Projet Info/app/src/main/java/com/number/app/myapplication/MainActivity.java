package com.number.app.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.number.app.myapplication.DB.DB_Data;
import com.number.app.myapplication.MedScreen.MedScreenFirstActivity;
import com.number.app.myapplication.PatScreen.PatFirstScreenActivity;
import com.number.app.myapplication.Util.NagivationClass;
import com.number.app.myapplication.Util.StaticValues;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                StartApp();

            }
        }, 2000);

    }

    private void StartApp() {

        if (!DB_Data.getInstance(this).getNote(StaticValues.reg_nat).equals("") && !DB_Data.getInstance(this).getNote(StaticValues.password_root).equals("") ) { //already login

            databaseReference.child(StaticValues.users_root).child(DB_Data.getInstance(this).getNote(StaticValues.reg_nat)).child(StaticValues.users_med_root).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.getValue().toString().equals("1"))
                    {
                        new LovelyStandardDialog(MainActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                                .setTopColorRes(R.color.colorPrimary)
                                .setButtonsColorRes(R.color.colorPrimary)
                                .setTitle("Medecin")
                                .setMessage("Voulez-vous accéder à l'interface médecin ou patient?")
                                .setPositiveButton("Médecin", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        NagivationClass.SendUserToOtherActivity(MainActivity.this, MedScreenFirstActivity.class, MainActivity.this);
                                    }
                                })
                                .setNegativeButton("Patient", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        NagivationClass.SendUserToOtherActivity(MainActivity.this, PatFirstScreenActivity.class, MainActivity.this);

                                    }
                                })
                                .show();
                    }
                    else
                    {
                        NagivationClass.SendUserToOtherActivityAndFinishThisActivity(MainActivity.this, PatFirstScreenActivity.class, MainActivity.this);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else //go to login
        {
            NagivationClass.SendUserToOtherActivityAndFinishThisActivity(MainActivity.this, LoginScreenActivity.class, MainActivity.this);
        }
    }
}