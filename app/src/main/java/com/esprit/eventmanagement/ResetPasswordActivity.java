package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputEditText tiPassword, tiConfirmPassword;
    private Button btnSubmit;
    private AppDataBase database;
    private String phoneNumber; // Numéro de téléphone de l'utilisateur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        tiPassword = findViewById(R.id.tiPassword);
        tiConfirmPassword = findViewById(R.id.tiConfirmPassword);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Initialiser la base de données
        database = AppDataBase.getAppDataBase(this);

        // Récupérer le numéro de téléphone de l'intention
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        btnSubmit.setOnClickListener(v -> handleChangePassword());
    }

    private void handleChangePassword() {
        String password = tiPassword.getText().toString();
        String confirmPassword = tiConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            tiConfirmPassword.setError("Les mots de passe ne correspondent pas");
            return;
        }

        if (password.length() < 6) {
            tiPassword.setError("Le mot de passe doit contenir au moins 6 caractères");
            return;
        }

        new Thread(() -> {
            // Récupérez l'utilisateur pour vérifier s'il possède bien un numéro de téléphone
            User user = database.UserDAO().getUserByPhoneNumber(phoneNumber);
            if (user != null) {
                // Affichez l'utilisateur dans les logs pour vérification
                Log.d("ResetPasswordActivity", "Utilisateur récupéré : " + user.toString());
            } else {
                Log.d("ResetPasswordActivity", "Aucun utilisateur trouvé avec le numéro de téléphone : " + phoneNumber);
            }

            // Mise à jour du mot de passe de l'utilisateur si l'utilisateur existe
            int rowsUpdated = database.UserDAO().updatePasswordByPhoneNumber(phoneNumber, password);
            runOnUiThread(() -> {
                if (rowsUpdated > 0) {
                    Toast.makeText(ResetPasswordActivity.this, "Mot de passe changé avec succès", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Erreur lors de la mise à jour du mot de passe", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}