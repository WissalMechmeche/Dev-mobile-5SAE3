package com.example.blogapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button open =findViewById(R.id.home);
        open.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent); // go to HomepageActivity
        });
//        Button openBlogFragmentButton = findViewById(R.id.openBlogFragmentButton);
//        openBlogFragmentButton.setOnClickListener(v -> {
//            BlogFragment blogFragment = new BlogFragment();
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.main, blogFragment); // Replaces main layout with BlogFragment
//            transaction.addToBackStack(null); // Adds the transaction to the back stack so user can navigate back
//            transaction.commit();
//        });
    }
}
