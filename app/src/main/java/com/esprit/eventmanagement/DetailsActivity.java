package com.esprit.eventmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {


    private TextView ticketInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ticketInfoText = findViewById(R.id.ticketInfoText);

        String eventName = getIntent().getStringExtra("eventName");
        String quantity = getIntent().getStringExtra("quantity");
        String ticketType = getIntent().getStringExtra("ticketType");

        String ticketDetails = "Event: " + eventName + "\nQuantity: " + quantity + "\nTicket Type: " + ticketType;
        ticketInfoText.setText(ticketDetails);



        Button addToCartBtn = findViewById(R.id.addToCardBtn);
        addToCartBtn.setOnClickListener(v -> {
        });
    }
}
