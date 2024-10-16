package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnSignUp = findViewById(R.id.btnCreateAccount);
        Button btnForgetPassword = findViewById(R.id.btnForgotPassword);
        Button btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(v -> {
            // Redirection vers HomeActivity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Optionnel : termine LoginActivity
        });

        btnSignUp.setOnClickListener(v -> {
            // Naviguer vers l'activité d'inscription
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        btnForgetPassword.setOnClickListener(v -> {
            Log.d("LoginActivity", "Forgot Password button clicked");
            // Naviguer vers l'activité de mot de passe oublié
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });

    }
}