package com.example.finalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.finalproject.databinding.ActivityMainBinding;
import com.example.finalproject.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bn;

    //private List<item> itemList;
//TODO FINISH MAIN PAGE  AND PROFILE PAGE AND ADD ITEMS TO LIST
    //ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding= MainActivity.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // login frag
        LoginFragment LoginFragment = new LoginFragment();
        fragmentTransaction.replace(R.id.mainFrame, LoginFragment);
        fragmentTransaction.commit();

        // home page
//        HomeFragment HomeFragment = new HomeFragment();
//        fragmentTransaction.replace(R.id.mainFrame, HomeFragment);
//        fragmentTransaction.commit();


        bn = findViewById(R.id.bNavBarMain);

        bn.setOnItemSelectedListener(item ->{

           switch (item.getTitle().toString()){

               case "Profile":
               {
                   replaceFragment(new ProfileFragment());
                   return true;

               }
               case "Settings":{
                   replaceFragment(new SettingsFragment());
                   return true;

               }
               case "Home":
               default:{
                   replaceFragment(new HomeFragment());
                   return true;

               }

           }
           
        } );

    }
    private void initMenu() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}