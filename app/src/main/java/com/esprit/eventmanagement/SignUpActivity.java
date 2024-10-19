package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

        // Find the new button
        Button btnAlreadyHaveAccount = findViewById(R.id.btnAlreadyHaveAccount);

        // Set an OnClickListener to navigate to LoginActivity12
        btnAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity so the user can't navigate back to it
            }
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
                    User newUser = new User(email, password); // Vous pouvez remplacer par login et pwd
                    database.UserDAO().addUSer(newUser);
                    Log.d("SignUpActivity", "User added to the database: " + newUser.toString());

                    runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Utilisateur ajouté avec succès!", Toast.LENGTH_SHORT).show());
                });
                // Redirigez vers l'activité de connexion
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); //
            } else {
                Log.d("SignUpActivity", "Login or password is empty, user not added.");
            }
        });

    }
        // Méthode pour valider les champs
        private boolean validateInputs(String fullName, String email, String password, String confirmPassword) {
            if (TextUtils.isEmpty(fullName)) {
                tiFullName.setError("Le nom complet est requis");
                return false;
            }
            if (TextUtils.isEmpty(email)) {
                tiEmail.setError("L'email est requis");
                return false;
            }
            if (TextUtils.isEmpty(password)) {
                tiPassword.setError("Le mot de passe est requis");
                return false;
            }
            if (!password.equals(confirmPassword)) {
                tiConfirmPassword.setError("Les mots de passe ne correspondent pas");
                return false;
            }
            return true;
        }


}