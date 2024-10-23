package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button submitButton;
    private Button sendSMSButton;
    private TwilioSMSService twilioSMSService; // Instance of your Twilio service

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);

        // Initialize the Twilio SMS Service
        twilioSMSService = new TwilioSMSService();

        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        emailEditText = findViewById(R.id.tiEmail);
        submitButton = findViewById(R.id.btnSubmit);
        sendSMSButton = findViewById(R.id.btnSendSMS);

        submitButton.setOnClickListener(v -> handleSubmit());
        sendSMSButton.setOnClickListener(v -> handleSendSMS());
    }

    private void handleSubmit() {
        String emailOrPhone = emailEditText.getText().toString().trim();
        if (isValidEmailOrPhone(emailOrPhone)) {
            sendResetLink(emailOrPhone);  // Method to send an email
        } else {
            showToast("Veuillez entrer un e-mail ou un numéro de téléphone valide.");
        }
    }

    private void handleSendSMS() {
        String emailOrPhone = emailEditText.getText().toString().trim();
        if (isValidEmailOrPhone(emailOrPhone)) {
            // Use an ExecutorService to run the SMS sending on a background thread
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    twilioSMSService.sendSms(emailOrPhone, "Hello, this is a test message!"); // Replace with your code generation logic
                    runOnUiThread(() -> showToast("Un code de vérification a été envoyé à " + emailOrPhone));
                } catch (Exception e) {
                    runOnUiThread(() -> showToast("Erreur lors de l'envoi du SMS : " + e.getMessage()));
                }
            });
        } else {
            showToast("Veuillez entrer un e-mail ou un numéro de téléphone valide.");
        }
    }

    private boolean isValidEmailOrPhone(String input) {
        // Check if the input is a valid email or phone number
        return input.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") || input.matches("^[2459][0-9]{7}$");
    }

    private void sendResetLink(String email) {
        // Implement the reset link email sending here
        showToast("Un lien de réinitialisation a été envoyé à " + email);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
