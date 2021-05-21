package com.number.app.myapplication.PatScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.number.app.myapplication.R;
import com.number.app.myapplication.Util.FragmentAdapter;

public class PatFirstScreenActivity extends AppCompatActivity {


    private FragmentAdapter FragAdapter;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pat_first_screen);
        SetViewPager();
    }

    private void SetViewPager()
    {
        viewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        toolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.main_tabs);



        setSupportActionBar(toolbar);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);
       // toolbar.setTitleTextAppearance(this, R.style.AveriaLight);
       // getSupportActionBar().setTitle("Mon profil"); //string is custom name you want

        getSupportActionBar().setTitle("Patient"); //string is custom name you want

        FragAdapter = new FragmentAdapter(getSupportFragmentManager(), 0);

       // Fragment HistoMed = new HistoriqueFragment();
       // ((FragmentAdapter) FragAdapter).AddFragment(HistoMed,"Medical History");
        Fragment ChronicDisease = new ChronicDiseaseFragment();
        ((FragmentAdapter) FragAdapter).AddFragment(ChronicDisease,"Chronic Disease");
        Fragment HistoPres = new HistoriquePrescriptionFragment();
        ((FragmentAdapter) FragAdapter).AddFragment(HistoPres,"Prescription History");
        Fragment Pres = new PrescriptionReceivedFragment();
        ((FragmentAdapter) FragAdapter).AddFragment(Pres,"Prescription received");


        viewPager.setAdapter(FragAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}