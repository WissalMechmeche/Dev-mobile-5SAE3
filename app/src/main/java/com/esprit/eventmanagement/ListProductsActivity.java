package com.esprit.eventmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.esprit.eventmanagement.Database.AppDatabase;
import com.esprit.eventmanagement.Entities.Ticket;

public class ListProductsActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "ticket-db").build();

        Button addButton = findViewById(R.id.btnAddTicket);
        addButton.setOnClickListener(v -> {
            EditText eventNameInput = findViewById(R.id.eventName);
            EditText quantityInput = findViewById(R.id.quantity);
            EditText ticketTypeInput = findViewById(R.id.ticketType);

            String eventName = eventNameInput.getText().toString();
            int quantity = Integer.parseInt(quantityInput.getText().toString());
            String ticketType = ticketTypeInput.getText().toString();

            insertTicket(eventName, quantity, ticketType);
        });
    }

    private void insertTicket(String eventName, int quantity, String ticketType) {
        new Thread(() -> {
            Ticket ticket = new Ticket(eventName, quantity, ticketType);
            db.ticketDao().insert(ticket);
        }).start();
    }
}