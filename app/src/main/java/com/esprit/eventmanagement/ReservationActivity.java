package com.esprit.eventmanagement;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.esprit.eventmanagement.database.AppDataBase;
import com.esprit.eventmanagement.entities.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {
    private Spinner ticketTypeSpinner;
    private Button addButton;
    private TextView totalTicketsTextView;
    private TextView totalPriceTextView;
    private List<Reservation> historyReservations;
    private ArrayAdapter<Reservation> adapter;
    private AppDataBase appDatabase;

    public ReservationActivity() {
        historyReservations = new ArrayList<>();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation); // Assurez-vous que le fichier XML est nommé activity_reservation.xml

        // Initialisation des vues
        ticketTypeSpinner = findViewById(R.id.ticket_type_spinner);
        addButton = findViewById(R.id.btn_add);
        totalTicketsTextView = findViewById(R.id.total_tickets);
        totalPriceTextView = findViewById(R.id.price_event);

        // Adapter pour Spinner (exemple avec des types de billets)
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.ticket_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ticketTypeSpinner.setAdapter(spinnerAdapter);

        // Initialisation de la base de données
        appDatabase = AppDataBase.getAppDataBase(this);

        // Initialisation de l'adaptateur pour la ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyReservations);
        ListView historyListView = findViewById(R.id.history_list_view);
        historyListView.setAdapter(adapter);

        // Événement au clic du bouton "Ajouter"
        addButton.setOnClickListener(v -> {
            String ticketType = ticketTypeSpinner.getSelectedItem().toString();

            // Créez une nouvelle réservation
            Reservation newReservation = new Reservation(ticketType, 1); // Exemple avec 1 billet réservé

            // Ajouter la réservation dans la base de données Room
            new Thread(() -> {
                appDatabase.ReservationDAO().insertReservation(newReservation); // Insérer la réservation dans la base de données
                runOnUiThread(() -> {
                    loadReservations();  // Charger les réservations et mettre à jour la vue
                    Toast.makeText(this, "Réservation ajoutée", Toast.LENGTH_SHORT).show();
                });
            }).start();
        });

        // Charger les réservations à l'ouverture de l'activité
        loadReservations();
    }

    private void loadReservations() {
        new Thread(() -> {
            // Récupérer les réservations depuis la base de données
            List<Reservation> reservations = appDatabase.ReservationDAO().getAllReservations();

            // Vérification des données récupérées
            Log.d("ReservationActivity", "Nombre de réservations récupérées : " + reservations.size());

            runOnUiThread(() -> {
                // S'assurer que l'on met bien à jour la liste
                historyReservations.clear();  // Nettoyer la liste avant de la remplir
                historyReservations.addAll(reservations);  // Ajouter toutes les réservations récupérées

                // Vérifier si les réservations ont bien été ajoutées
                Log.d("ReservationActivity", "Réservations ajoutées à l'historique");

                updateTotal();  // Mettre à jour le total des billets et le prix
                adapter.notifyDataSetChanged();  // Rafraîchir l'adaptateur pour afficher les nouvelles données
            });
        }).start();
    }

    private void updateTotal() {
        // Calculez le nombre total de billets réservés et le prix total
        int totalTickets = 0;
        double totalPrice = 0.0;
        for (Reservation reservation : historyReservations) {
            totalTickets += reservation.getNumberOfTickets();
            totalPrice += reservation.getNumberOfTickets() * 10.0;
        }

        totalTicketsTextView.setText("Total billets : " + totalTickets);
        totalPriceTextView.setText("Prix total : $" + totalPrice);
    }
}