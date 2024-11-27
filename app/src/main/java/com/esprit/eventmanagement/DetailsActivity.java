package com.esprit.eventmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.esprit.eventmanagement.DAO.TicketDao;
import com.esprit.eventmanagement.database.AppDataBase;
import com.esprit.eventmanagement.entities.Ticket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DetailsActivity extends Activity {
    private TicketDao ticketDao;
    private AppDataBase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        appDatabase = AppDataBase.getAppDataBase(this);  // Get the database instance
        ticketDao = appDatabase.TicketDao();  // Get the DAO instance

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
