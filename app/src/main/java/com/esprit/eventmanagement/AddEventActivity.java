package com.esprit.eventmanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.List;

import database.AppDataBase;
import entities.Event;

public class AddEventActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1; // Constant for image picking
    private TextView editTextTitle, editTextDescription, editTextStartDate, editTextEndDate;
    private ImageButton imageButtonAdd;
    private Button buttonAddEvent, buttonCancel, btnViewAllEvents;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_event);

        editTextTitle = findViewById(R.id.editTextText);
        editTextDescription = findViewById(R.id.editTextText2);
        editTextStartDate = findViewById(R.id.editTextDate);
        editTextEndDate = findViewById(R.id.editTextDate2);
        imageButtonAdd = findViewById(R.id.imageButtonAdd);
        buttonAddEvent = findViewById(R.id.button3);
        buttonCancel = findViewById(R.id.button4);
        btnViewAllEvents = findViewById(R.id.btnViewAllEvents);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageButtonAdd.setOnClickListener(v -> openImagePicker());
        buttonAddEvent.setOnClickListener(v -> addEvent());
        buttonCancel.setOnClickListener(v -> showCancelDialog());
        btnViewAllEvents.setOnClickListener(v -> viewAllEvents());

        setupDatePicker(editTextStartDate);
        setupDatePicker(editTextEndDate);
    }

    private void setupDatePicker(TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        dateTextView.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddEventActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        dateTextView.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                    },
                    year, month, day);
            datePickerDialog.show();
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
        }
    }

    private void addEvent() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String startDate = editTextStartDate.getText().toString();
        String endDate = editTextEndDate.getText().toString();

        if (title.isEmpty() || description.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        if (imageUri != null) {
            event.setImagePath(imageUri.toString());
        }

        AppDataBase db = AppDataBase.getAppDataBase(this);
        db.eventDAO().addEvent(event);

        List<Event> events = db.eventDAO().getAllEvents();
        Log.d("AddEventActivity", "Current events in database: " + events.size());

        Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void viewAllEvents() {
        AllEventsFragment allEventsFragment = new AllEventsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, allEventsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Event")
                .setMessage("Are you sure you want to cancel adding this event?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }
}
