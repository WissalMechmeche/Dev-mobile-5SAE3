package com.esprit.eventmanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.esprit.eventmanagement.database.AppDataBase;
import com.esprit.eventmanagement.entities.User;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail, etPassword, etConfirmPassword, etPhoneNumber;
    private User currentUser;
    private SessionManager sessionManager;
    private AppDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize fields
        etFullName = findViewById(R.id.tiFullName);
        etEmail = findViewById(R.id.tiEmail);
        etPhoneNumber = findViewById(R.id.tiPhoneNumber); // Nouveau champ pour le numéro de téléphone
        etPassword = findViewById(R.id.tiPassword);
        etConfirmPassword = findViewById(R.id.tiConfirmPassword);

        Button btnSaveChanges = findViewById(R.id.btnSaveChanges);


        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Initialize the Room database instance
        db = AppDataBase.getAppDataBase(this); // Initialize your database here

        // Retrieve current user data from session or database
        if (sessionManager.isLoggedIn()) {
            int userId = sessionManager.getUserId(); // Assuming you have this method to retrieve the user ID
            currentUser = db.UserDAO().getOne(userId);

            if (currentUser != null) {
                // Prepopulate the fields with the user's data
                etFullName.setText(currentUser.getFullName());
                etEmail.setText(currentUser.getEmail());
                etPhoneNumber.setText(currentUser.getPhoneNumber()); // Pré-remplir le numéro de téléphone
                etPassword.setText(currentUser.getPassword());
                etConfirmPassword.setText(currentUser.getPassword());
            }
        }

        // Save changes when the button is clicked
        btnSaveChanges.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Ajouter le préfixe +216 si nécessaire
            if (!phoneNumber.startsWith("+216")) {
                phoneNumber = "+216" + phoneNumber;
            }

            // Validate inputs before saving
            if (validateInputs(fullName, email, phoneNumber, password, confirmPassword)) {
                // Update the user object with new data
                currentUser.setFullName(fullName);
                currentUser.setEmail(email);
                currentUser.setPhoneNumber(phoneNumber);

                if (!TextUtils.isEmpty(password)) {
                    currentUser.setPassword(password);
                }

                // Update the user in the database
                db.UserDAO().updateUSer(currentUser);
                Toast.makeText(EditProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to validate the inputs
    private boolean validateInputs(String fullName, String email, String phoneNumber, String password, String confirmPassword) {
        boolean isValid = true;

        // Full Name validation
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full Name is required");
            isValid = false;
        } else {
            etFullName.setError(null);
        }

        // Email validation
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            isValid = false;
        } else {
            etEmail.setError(null);
        }

        // Phone Number validation
        if (TextUtils.isEmpty(phoneNumber)) {
            etPhoneNumber.setError("Phone Number is required");
            isValid = false;
        } else if (!android.util.Patterns.PHONE.matcher(phoneNumber).matches()) {
            etPhoneNumber.setError("Please enter a valid phone number");
            isValid = false;
        } else {
            etPhoneNumber.setError(null);
        }

        // Password validation
        if (!TextUtils.isEmpty(password) && password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            etPassword.setError(null);
        }

        // Confirm Password validation
        if (!TextUtils.isEmpty(password) && !password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            isValid = false;
        } else {
            etConfirmPassword.setError(null);
        }

        return isValid;
    }
}