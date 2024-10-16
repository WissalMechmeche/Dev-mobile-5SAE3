package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);

        // Find the return button
        Button btnReturn = findViewById(R.id.btnReturn);

        // Set up a click listener to navigate back to LoginActivity
        btnReturn.setOnClickListener(v -> {
            // Redirect to LoginActivity
            Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            // Optionally, finish the current activity if you don't want to allow going back
            finish();
        });
    }
}