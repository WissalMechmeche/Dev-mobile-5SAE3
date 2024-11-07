package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button sendEmailButton;
    private Button sendSMSButton ;
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
        sendEmailButton = findViewById(R.id.btnSendEmail);
        sendSMSButton = findViewById(R.id.btnSendSMS);

        sendEmailButton.setOnClickListener(v -> handleSubmit());
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

    private String verificationCode; // Stocker le code généré

    private void handleSendSMS() {
        String emailOrPhone = emailEditText.getText().toString().trim();
        if (isValidEmailOrPhone(emailOrPhone)) {
            if (!emailOrPhone.startsWith("+")) {
                emailOrPhone = "+216" + emailOrPhone;
            }

            // Générer un code aléatoire de 4 chiffres
            verificationCode = String.format("%04d", new Random().nextInt(10000));

            ExecutorService executor = Executors.newSingleThreadExecutor();
            String finalPhoneNumber = emailOrPhone;
            executor.execute(() -> {
                try {
                    // Envoyer le code par SMS
                    twilioSMSService.sendSms(finalPhoneNumber, "Votre code de vérification est : " + verificationCode);

                    runOnUiThread(() -> {
                        showToast("Un code de vérification a été envoyé à " + finalPhoneNumber);

                        // Redirection vers l'activité de validation avec le code comme extra
                        Intent intent = new Intent(ForgetPasswordActivity.this, ValidationActivity.class);
                        intent.putExtra("verificationCode", verificationCode); // Passer le code de vérification
                        intent.putExtra("phoneNumber", finalPhoneNumber); // Passer le numéro de téléphone
                        startActivity(intent);
                        finish(); // Terminer l'activité actuelle si nécessaire
                    });}
                catch (Exception e) {
                    runOnUiThread(() -> showToast("Erreur lors de l'envoi du SMS : " + e.getMessage()));
                }
            });
        } else {
            showToast("Veuillez entrer un e-mail ou un numéro de téléphone valide.");
        }
    }
    private void sendResetLink(String email) {
        // Simulez l'envoi de l'e-mail ici avec un message personnalisé
        new Thread(() -> {
            // Générez un code de réinitialisation ou un lien unique
            String resetCode = String.format("%06d", new Random().nextInt(1000000));

            // Ajoutez le lien ou le code au message
            String message = "Cliquez sur le lien suivant pour réinitialiser votre mot de passe : " +
                    "https://www.votreapp.com/reset_password?email=" + email + "&code=" + resetCode;

            // Simulez l'envoi de l'e-mail
            boolean emailSent = sendEmail(email, "Réinitialisation de mot de passe", message);

            // Met à jour l'interface après l'envoi de l'e-mail
            runOnUiThread(() -> {
                if (emailSent) {
                    showToast("Un e-mail de réinitialisation a été envoyé à " + email);
                } else {
                    showToast("Erreur lors de l'envoi de l'e-mail");
                }
            });
        }).start();
    }

    // Simulez une méthode pour envoyer un e-mail
    private boolean sendEmail(String to, String subject, String message) {
        // Intégrez ici l'API d'envoi d'e-mails comme SendGrid, Mailgun, ou votre serveur SMTP
        // Retournez true si l'e-mail est envoyé avec succès, sinon false
        return true; // Simule un envoi réussi
    }




    private boolean isValidEmailOrPhone(String input) {
        // Check if the input is a valid email or phone number
        return input.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") || input.matches("^[2459][0-9]{7}$");
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
