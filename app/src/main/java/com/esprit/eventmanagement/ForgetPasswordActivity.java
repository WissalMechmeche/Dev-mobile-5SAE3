package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.esprit.eventmanagement.database.AppDataBase;
import com.esprit.eventmanagement.entities.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForgetPasswordActivity extends AppCompatActivity {

    private TextInputEditText phoneEditText;
    private Button sendSMSButton ;
    private TwilioSMSService twilioSMSService; // Instance of your Twilio service

    private AppDataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);

        database = AppDataBase.getAppDataBase(this);

        // Initialize the Twilio SMS Service
        twilioSMSService = new TwilioSMSService();

        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        phoneEditText = findViewById(R.id.tiEmail);
        sendSMSButton = findViewById(R.id.btnSendSMS);

        sendSMSButton.setOnClickListener(v -> handleSendSMS());
    }



    private String verificationCode; // Stocker le code généré

    private void handleSendSMS() {
        String emailOrPhone = phoneEditText.getText().toString().trim();
        if (isValidPhone(emailOrPhone)) {
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
                    User user = database.UserDAO().getUserByPhoneNumber(finalPhoneNumber);
                    twilioSMSService.sendSms(finalPhoneNumber,user.getFullName(),verificationCode );


                    runOnUiThread(() -> {
                        showToast("Un code de vérification a été envoyé à " + finalPhoneNumber);

                        // Redirection vers l'activité de validation avec le code comme extra
                        Intent intent = new Intent(ForgetPasswordActivity.this, ValidationActivity.class);
                        intent.putExtra("verificationCode", verificationCode); // Passer le code de vérification
                        intent.putExtra("phoneNumber", finalPhoneNumber); // Passer le numéro de téléphone
                        intent.putExtra("fullName", user.getFullName());
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



    private boolean isValidPhone(String input) {
        // Check if the input is a valid email or phone number
        return  input.matches("^[2459][0-9]{7}$");
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
