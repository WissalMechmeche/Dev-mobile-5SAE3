package com.esprit.eventmanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.esprit.eventmanagement.database.AppDataBase;
import com.esprit.eventmanagement.entities.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationFragment extends Fragment {

    private Spinner ticketTypeSpinner;
    private Button addButton;
    private TextView totalTicketsTextView;
    private TextView totalPriceTextView;
    private List<Reservation> historyReservations;
    private ArrayAdapter<Reservation> adapter;
    private AppDataBase appDatabase;

    public ReservationFragment() {
        historyReservations = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        // Initialisation des vues
        ticketTypeSpinner = view.findViewById(R.id.ticket_type_spinner);
        addButton = view.findViewById(R.id.btn_add);
        totalTicketsTextView = view.findViewById(R.id.total_tickets);
        totalPriceTextView = view.findViewById(R.id.price_event);

        // Adapter pour Spinner (exemple avec des types de billets)
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.ticket_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ticketTypeSpinner.setAdapter(spinnerAdapter);

        // Initialisation de la base de données
        appDatabase = AppDataBase.getAppDataBase(getContext());

        // Initialisation de l'adaptateur pour la ListView
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, historyReservations);
        ListView historyListView = view.findViewById(R.id.history_list_view);
        historyListView.setAdapter(adapter);

        // Événement au clic du bouton "Ajouter"
        addButton.setOnClickListener(v -> {
            String ticketType = ticketTypeSpinner.getSelectedItem().toString();

            // Créez une nouvelle réservation
            Reservation newReservation = new Reservation(ticketType, 1); // Exemple avec 1 billet réservé

            // Ajouter la réservation dans la base de données Room
            new Thread(new Runnable() {
                @Override
                public void run() {
                    appDatabase.ReservationDAO().insertReservation(newReservation); // Insérer la réservation dans la base de données
                    getActivity().runOnUiThread(() -> {
                        loadReservations();  // Charger les réservations et mettre à jour la vue
                        Toast.makeText(getContext(), "Réservation ajoutée", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        });

        return view;
    }

    private void loadReservations() {
        // Vérification que l'adaptateur est bien attaché à la ListView
        if (adapter == null) {
            Log.e("ReservationFragment", "L'adaptateur n'est pas initialisé !");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Récupérer les réservations depuis la base de données
                List<Reservation> reservations = appDatabase.ReservationDAO().getAllReservations();

                // Vérification des données récupérées
                Log.d("ReservationFragment", "Nombre de réservations récupérées : " + reservations.size());

                getActivity().runOnUiThread(() -> {
                    // S'assurer que l'on met bien à jour la liste
                    historyReservations.clear();  // Nettoyer la liste avant de la remplir
                    historyReservations.addAll(reservations);  // Ajouter toutes les réservations récupérées

                    // Vérifier si les réservations ont bien été ajoutées
                    Log.d("ReservationFragment", "Réservations ajoutées à l'historique");

                    updateTotal();  // Mettre à jour le total des billets et le prix
                    adapter.notifyDataSetChanged();  // Rafraîchir l'adaptateur pour afficher les nouvelles données
                });
            }
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
