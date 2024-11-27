package com.esprit.eventmanagement;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esprit.eventmanagement.database.AppDataBase;
import com.esprit.eventmanagement.entities.Reservation;

public class activity_detail_reservation extends AppCompatActivity {

    private TextView ticketTypeTextView, numTicketsTextView, totalPriceTextView;
    private Button cancelButton;
    private AppDataBase appDatabase;

    private static final String TAG = "ActivityDetailReservation";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reservation);

        // Initialisation des vues
        ticketTypeTextView = findViewById(R.id.ticket_type);
        numTicketsTextView = findViewById(R.id.num_tickets);
        totalPriceTextView = findViewById(R.id.price_event);
        cancelButton = findViewById(R.id.cancel_button);

        // Vérification que les vues sont bien initialisées
        if (ticketTypeTextView == null || numTicketsTextView == null || totalPriceTextView == null || cancelButton == null) {
            Toast.makeText(this, "Erreur d'initialisation des vues.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialisation de la base de données
        appDatabase = AppDataBase.getAppDataBase(this);

        // Récupération de l'ID de la réservation
        int reservationId = getIntent().getIntExtra("reservation_id", -1);
        if (reservationId != -1) {
            loadReservationDetails(reservationId);
        } else {
            Toast.makeText(this, "ID de réservation invalide", Toast.LENGTH_SHORT).show();
        }

        // Gestion du clic sur le bouton annuler
        cancelButton.setOnClickListener(v -> cancelReservation(reservationId));
    }

    private void loadReservationDetails(int reservationId) {
        new Thread(() -> {
            // Récupération de la réservation depuis la base de données
            Reservation reservation = appDatabase.ReservationDAO().getReservationById(reservationId);
            runOnUiThread(() -> {
                if (reservation != null) {
                    // Mise à jour des vues avec les détails de la réservation
                    ticketTypeTextView.setText("Type de Billet: " + reservation.getTicketType());
                    numTicketsTextView.setText("Nombre de Billets: " + reservation.getNumberOfTickets());
                    totalPriceTextView.setText("Prix: $" + (reservation.getNumberOfTickets() * 50)); // Prix fixe par billet
                } else {
                    Toast.makeText(activity_detail_reservation.this, "Réservation non trouvée.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void cancelReservation(int reservationId) {
        Log.d(TAG, "Tentative de suppression de la réservation avec l'ID: " + reservationId);

        new Thread(() -> {
            // Suppression de la réservation de la base de données
            int rowsDeleted = appDatabase.ReservationDAO().deleteReservationById(reservationId);

            runOnUiThread(() -> {
                if (rowsDeleted > 0) {
                    Log.d(TAG, "Réservation supprimée avec succès.");
                    // Afficher un message à l'utilisateur
                    Toast.makeText(activity_detail_reservation.this, "Réservation annulée", Toast.LENGTH_SHORT).show();
                    // Retour à l'écran principal ou mettre à jour l'UI selon le cas
                    finish();  // Ferme l'écran actuel pour retourner à l'écran précédent (ou l'écran principal)
                } else {
                    Log.d(TAG, "Erreur lors de la suppression de la réservation.");
                    Toast.makeText(activity_detail_reservation.this, "Erreur lors de l'annulation de la réservation.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}