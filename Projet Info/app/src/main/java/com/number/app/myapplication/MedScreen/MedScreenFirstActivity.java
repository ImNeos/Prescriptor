package com.number.app.myapplication.MedScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.number.app.myapplication.R;
import com.number.app.myapplication.Util.FragmentAdapter;

public class MedScreenFirstActivity extends AppCompatActivity {

    private FragmentAdapter FragAdapter;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_screen_first);
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


        FragAdapter = new FragmentAdapter(getSupportFragmentManager(), 0);

        Fragment PatFrag = new PatFragment();
        ((FragmentAdapter) FragAdapter).AddFragment(PatFrag,"List patients");
        Fragment PatDem = new PatDemandFragment();
        ((FragmentAdapter) FragAdapter).AddFragment(PatDem,"List demandes");

        getSupportActionBar().setTitle("Medecin"); //string is custom name you want


        viewPager.setAdapter(FragAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}