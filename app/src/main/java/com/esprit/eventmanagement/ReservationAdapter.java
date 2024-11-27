package com.esprit.eventmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.eventmanagement.database.AppDataBase;
import com.esprit.eventmanagement.entities.Reservation;

import java.util.List;

public class ReservationAdapter extends ArrayAdapter<Reservation> {
    private Context context;
    private List<Reservation> reservations;

    public ReservationAdapter(Context context, List<Reservation> reservations) {
        super(context, 0, reservations);
        this.context = context;
        this.reservations = reservations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        }

        Reservation reservation = getItem(position);

        // Find views with null checks to avoid NullPointerException
        ImageView imageEvent = view.findViewById(R.id.image_event);
        TextView priceEvent = view.findViewById(R.id.price_event);
        TextView availableSeats = view.findViewById(R.id.available_seats);
        Button detailButton = view.findViewById(R.id.detail_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        // Ensure the views are not null before setting text or doing operations
        if (reservation != null) {
            if (priceEvent != null) {
                priceEvent.setText("Prix : $" + (reservation.getNumberOfTickets() * 50)); // Adjust to actual pricing logic
            }

            if (availableSeats != null) {
                availableSeats.setText("Nombre de places : " + reservation.getTicketType()); // Example field
            }

            // Handle event image if it's not null
            if (imageEvent != null) {
                // Set event image if the reservation contains a valid image URI (uncomment if needed)
                // imageEvent.setImageURI(Uri.parse(reservation.getEventImage()));
            }

            if (detailButton != null) {
                detailButton.setOnClickListener(v -> {
                    Intent intent = new Intent(context, activity_detail_reservation.class);
                    intent.putExtra("reservation_id", reservation.getId());
                    context.startActivity(intent);
                });
            }

            if (cancelButton != null) {
                cancelButton.setOnClickListener(v -> {
                    deleteReservation(reservation.getId());
                });
            }
        }

        return view;
    }

    private void deleteReservation(int reservationId) {
        AppDataBase appDatabase = AppDataBase.getAppDataBase(context);
        new Thread(() -> {
            // Supprimer la réservation de la base de données
            appDatabase.ReservationDAO().deleteReservationById(reservationId);

            // Mettre à jour l'UI sur le thread principal
            ((Activity) context).runOnUiThread(() -> {
                // Retirer la réservation de la liste et notifier l'adaptateur
                reservations.removeIf(res -> res.getId() == reservationId);
                // Vérifiez si l'adaptateur n'est pas null avant de mettre à jour
                if (reservations != null) {
                    notifyDataSetChanged();
                }
            });
        }).start();
    }
}
