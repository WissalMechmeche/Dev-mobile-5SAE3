package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.esprit.eventmanagement.database.AppDataBase;
import com.esprit.eventmanagement.entities.User;
import com.google.android.material.textfield.TextInputEditText;
import android.text.TextUtils;
import android.util.Patterns;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this); // Initialize SessionManager

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            // Redirect to HomeActivity if the user is already logged in
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return; // Stop further execution of onCreate
        }

        // If the user is not logged in, display the login layout
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.tiEmail);
        etPassword = findViewById(R.id.tiPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignUp = findViewById(R.id.btnCreateAccount);
        Button btnForgetPassword = findViewById(R.id.btnForgotPassword);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Call the validation function
            if (validateInputs(email, password)) {
                // Check login information
                AppDataBase db = AppDataBase.getAppDataBase(this);
                User user = db.UserDAO().getUserByEmailAndPassword(email, password);

                if (user != null) {
                    // Save user session
                    sessionManager.saveUserSession(user.getId(), user.getEmail());

                    // Redirect to HomeActivity
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // Prevent returning to the login screen
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        btnForgetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });
    }

    // Function to validate email and password inputs
    private boolean validateInputs(String email, String password) {
        boolean isValid = true;

        // Email validation
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            isValid = false;
        } else {
            etEmail.setError(null);
        }

        // Password validation
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            isValid = false;
        } else {
            etPassword.setError(null);
        }

        return isValid;
    }
}
