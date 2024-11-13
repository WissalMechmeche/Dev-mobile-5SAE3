package com.esprit.eventmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esprit.eventmanagement.Dao.TicketDao;
import com.esprit.eventmanagement.Database.AppDatabase;
import com.esprit.eventmanagement.Entities.Ticket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DetailsActivity extends AppCompatActivity {
    private TicketDao ticketDao;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        appDatabase = AppDatabase.getInstance(this);  // Get the database instance
        ticketDao = appDatabase.ticketDao();  // Get the DAO instance

        Button deleteButton = findViewById(R.id.deleteButton); // Assuming you add a delete button in your XML
        int ticketId = getIntent().getIntExtra("TICKET_ID", -1);  // Get the ticket ID passed from the previous activity

        // Button click listener to delete the ticket
        deleteButton.setOnClickListener(v -> {
            if (ticketId != -1) {
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    Ticket ticket = ticketDao.getTicketById(ticketId);
                    if (ticket != null) {
                        ticketDao.deleteById(ticketId);
                        runOnUiThread(() -> {
                            Toast.makeText(DetailsActivity.this, "Ticket deleted", Toast.LENGTH_SHORT).show();
                            finish();  // Optionally finish the activity to return to the previous screen
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(DetailsActivity.this, "Ticket not found", Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });
    }
}