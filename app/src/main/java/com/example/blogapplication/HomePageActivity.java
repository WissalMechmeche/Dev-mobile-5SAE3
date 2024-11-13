package com.example.blogapplication;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.blogapplication.databinding.ActivityHomePageBinding;

public class HomePageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button openBlogFragmentButton = findViewById(R.id.openBlogFragmentButton);
        openBlogFragmentButton.setOnClickListener(v -> {
            BlogFragment blogFragment = new BlogFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main, blogFragment); // Replaces main layout with BlogFragment
            transaction.addToBackStack(null); // Adds the transaction to the back stack so user can navigate back
            transaction.commit();
        });
    }


}