package com.example.myprogect.Sellers;


import android.content.Intent;
import android.os.Bundle;

import com.example.myprogect.Buyers.MainActivity;
import com.example.myprogect.R;
import com.example.myprogect.Sellers.SellersFragment.CustomerCerviceFragment;
import com.example.myprogect.Sellers.SellersFragment.MyProductsFrgment;
import com.example.myprogect.Sellers.SellersFragment.MySalesFragment;
import com.example.myprogect.Sellers.SellersFragment.SettingsFragment;


import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;


import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;

public class SellersHomeMainActivity extends AppCompatActivity {

    SpaceTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_home_main);




        List fragmentList = new ArrayList<>();

        fragmentList.add(new CustomerCerviceFragment());
        fragmentList.add(new MyProductsFrgment());
        fragmentList.add(new MySalesFragment());
        fragmentList.add(new SettingsFragment());




        final CoordinatorLayout coordinatorLayout = findViewById(R.id.activity_sellers_home_main);

        ViewPager viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.spaceTabLayout);


        tabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, savedInstanceState);

        tabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tabLayout.setTabOneOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Welcome to SpaceTabLayout", Snackbar.LENGTH_SHORT);

                snackbar.show();
            }
        });

        tabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "" + tabLayout.getCurrentPosition(), Toast.LENGTH_SHORT).show();
            }
        });
    }







}

