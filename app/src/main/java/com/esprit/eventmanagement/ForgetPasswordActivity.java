package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button submitButton;
    private Button sendSMSButton;

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

        emailEditText = findViewById(R.id.tiEmail);
        submitButton = findViewById(R.id.btnSubmit);
        sendSMSButton = findViewById(R.id.btnSendSMS);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailOrPhone = emailEditText.getText().toString().trim();
                if (isValidEmailOrPhone(emailOrPhone)) {
                    sendResetLink(emailOrPhone);  // Méthode pour envoyer un e-mail
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Veuillez entrer un e-mail ou un numéro de téléphone valide.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sendSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailOrPhone = emailEditText.getText().toString().trim();
                if (isValidEmailOrPhone(emailOrPhone)) {
                    sendSMSVerification(emailOrPhone);  // Méthode pour envoyer un SMS
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Veuillez entrer un e-mail ou un numéro de téléphone valide.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidEmailOrPhone(String input) {
        // Vérifiez si l'entrée est un e-mail ou un numéro de téléphone valide
        return input.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") || input.matches("^[2459][0-9]{7}$"); // Exemple de validation
    }


    private void sendResetLink(String email) {
        // Implémentez l'envoi d'un e-mail de réinitialisation ici
        // Exemple : utiliser une API pour envoyer l'e-mail
        Toast.makeText(this, "Un lien de réinitialisation a été envoyé à " + email, Toast.LENGTH_SHORT).show();
    }

    private void sendSMSVerification(String phoneNumber) {
        // Implémentez l'envoi d'un SMS de vérification ici
        // Exemple : utiliser une API pour envoyer le SMS
        Toast.makeText(this, "Un code de vérification a été envoyé à " + phoneNumber, Toast.LENGTH_SHORT).show();
    }
}