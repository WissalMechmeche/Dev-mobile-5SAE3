package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.esprit.eventmanagement.database.AppDataBase;
import com.esprit.eventmanagement.entities.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {
    private EditText tiFullName, tiEmail, tiPassword, tiConfirmPassword;
    private Button btnSignUp;
    private AppDataBase database;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        Button btnAlreadyHaveAccount = findViewById(R.id.btnAlreadyHaveAccount);

        btnAlreadyHaveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Initialiser les champs
        tiFullName = findViewById(R.id.tiFullName);
        tiEmail = findViewById(R.id.tiEmail);
        tiPassword = findViewById(R.id.tiPassword);
        tiConfirmPassword = findViewById(R.id.tiConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Initialiser la base de données
        database = AppDataBase.getAppDataBase(this);

        // Gestion du clic sur le bouton S'inscrire
        btnSignUp.setOnClickListener(view -> {
            String fullName = tiFullName.getText().toString().trim();
            String email = tiEmail.getText().toString().trim();
            String password = tiPassword.getText().toString().trim();
            String confirmPassword = tiConfirmPassword.getText().toString().trim();

            if (validateInputs(fullName, email, password, confirmPassword)) {
                // Ajouter l'utilisateur dans la base de données
                executorService.execute(() -> {
                    User newUser = new User(email, password);
                    database.UserDAO().addUSer(newUser);
                    Log.d("SignUpActivity", "User added to the database: " + newUser.toString());

                    runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "User added successfully!", Toast.LENGTH_SHORT).show());
                });
                // Redirigez vers l'activité de connexion
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.d("SignUpActivity", "Full name, email or password is empty, user not added.");
            }
        });
    }

    private boolean validateInputs(String fullName, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(fullName)) {
            tiFullName.setError("Full name is required");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            tiEmail.setError("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tiEmail.setError("Please enter a valid email");
            return false;
        }
        List<String> existingEmails = database.UserDAO().getAllEmails();
        if (existingEmails.contains(email)) {
            tiEmail.setError("This email is already in use");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            tiPassword.setError("Password is required");
            return false;
        } else if (password.length() < 6) {
            tiPassword.setError("Password must be at least 6 characters long");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            tiConfirmPassword.setError("Passwords do not match");
            return false;
        }

        return true;
    }
}
