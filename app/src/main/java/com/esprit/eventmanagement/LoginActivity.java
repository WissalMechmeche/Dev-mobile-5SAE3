package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnSignUp = findViewById(R.id.btnCreateAccount);
        Button btnForgetPassword = findViewById(R.id.btnForgotPassword);

        btnSignUp.setOnClickListener(v -> {
            Log.d("LoginActivity", "Sign Up button clicked");
            // Naviguer vers l'activité d'inscription
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        btnForgetPassword.setOnClickListener(v -> {
            Log.d("LoginActivity", "Forgot Password button clicked");
            // Naviguer vers l'activité de mot de passe oublié
            Intent intent = new Intent(MainActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });

    }
}