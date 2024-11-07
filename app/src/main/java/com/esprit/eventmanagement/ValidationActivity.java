package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class ValidationActivity extends AppCompatActivity {

    private TextInputEditText tiCode1, tiCode2, tiCode3, tiCode4;
    private Button btnVerify;
    private String verificationCode;
    private String phoneNumber ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);




        tiCode1 = findViewById(R.id.tiCode1);
        tiCode2 = findViewById(R.id.tiCode2);
        tiCode3 = findViewById(R.id.tiCode3);
        tiCode4 = findViewById(R.id.tiCode4);
        btnVerify = findViewById(R.id.btnVerify);

        // Récupérer le code de vérification envoyé depuis ForgetPasswordActivity
        verificationCode = getIntent().getStringExtra("verificationCode");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        btnVerify.setOnClickListener(v -> verifyCode());
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
}