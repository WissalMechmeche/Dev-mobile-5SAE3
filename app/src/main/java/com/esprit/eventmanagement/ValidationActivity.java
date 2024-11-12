package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ValidationActivity extends AppCompatActivity {

    private TextInputEditText tiCode1, tiCode2, tiCode3, tiCode4;
    private Button btnVerify , btnResendCode;
    private String verificationCode;
    private String phoneNumber ;
    private String fullName ;
    private TwilioSMSService twilioSMSService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        twilioSMSService = new TwilioSMSService();


        tiCode1 = findViewById(R.id.tiCode1);
        tiCode2 = findViewById(R.id.tiCode2);
        tiCode3 = findViewById(R.id.tiCode3);
        tiCode4 = findViewById(R.id.tiCode4);
        btnVerify = findViewById(R.id.btnVerify);
        btnResendCode = findViewById(R.id.btnResendCode);

        // Récupérer le code de vérification envoyé depuis ForgetPasswordActivity
        verificationCode = getIntent().getStringExtra("verificationCode");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        fullName = getIntent().getStringExtra("fullName");


        btnVerify.setOnClickListener(v -> verifyCode());
        btnResendCode.setOnClickListener(v -> resendCode());

    }

    private void verifyCode() {
        String code = tiCode1.getText().toString() +
                tiCode2.getText().toString() +
                tiCode3.getText().toString() +
                tiCode4.getText().toString();

        if (code.equals(verificationCode)) {
            Toast.makeText(this, "Code validé avec succès", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ValidationActivity.this, ResetPasswordActivity.class);
            intent.putExtra("phoneNumber", phoneNumber); // Passez l'email de l'utilisateur
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Code incorrect. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resendCode() {
        // Désactiver le bouton pendant 10 secondes
        btnResendCode.setEnabled(false);
        Toast.makeText(this, "Veuillez patienter 10 secondes pour renvoyer le code", Toast.LENGTH_SHORT).show();

        // Délai de 10 secondes avant de renvoyer le SMS
        new Handler().postDelayed(() -> {
            // Exécuter l'envoi du SMS dans un thread secondaire
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                String newCode = generateNewVerificationCode();
                twilioSMSService.sendSms(phoneNumber, fullName, newCode);

                // Mettre à jour le code de vérification localement
                verificationCode = newCode;

                // Réactiver le bouton et notifier l'utilisateur dans l'interface utilisateur
                runOnUiThread(() -> {
                    btnResendCode.setEnabled(true);
                    Toast.makeText(this, "Code renvoyé", Toast.LENGTH_SHORT).show();
                });
            });
        }, 10000); // 10000 ms = 10 secondes
    }

    private String generateNewVerificationCode() {
        // Générer un nouveau code de vérification de 4 chiffres
        return String.format("%04d", new Random().nextInt(10000));
    }
}