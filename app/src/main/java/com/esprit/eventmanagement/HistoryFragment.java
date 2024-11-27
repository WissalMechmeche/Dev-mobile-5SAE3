package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.esprit.eventmanagement.database.AppDataBase;
import com.esprit.eventmanagement.DAO.ReservationDAO;
import com.esprit.eventmanagement.entities.Reservation;

import java.util.List;
public class HistoryFragment extends Fragment {

    private ListView listView;
    private List<Reservation> reservations;
    private ReservationAdapter adapter;

    public HistoryFragment() {
        // Le constructeur par défaut
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        listView = view.findViewById(R.id.reservation_list);

        // Récupérer les données depuis la base de données dans un thread en arrière-plan
        AppDataBase database = AppDataBase.getAppDataBase(requireContext());
        ReservationDAO reservationDAO = database.ReservationDAO();

        // Exécution dans un thread en arrière-plan pour récupérer les réservations
        new Thread(new Runnable() {
            @Override
            public void run() {
                reservations = reservationDAO.getAllReservations(); // Récupérer toutes les réservations

                // Mettre à jour l'adaptateur sur le thread principal
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Assurez-vous que l'adaptateur est initialisé avant d'appeler notifyDataSetChanged()
                        if (adapter == null) {
                            adapter = new ReservationAdapter(requireContext(), reservations);
                            listView.setAdapter(adapter); // Assigner l'adaptateur à la ListView
                        } else {
                            adapter.notifyDataSetChanged();  // Rafraîchir la liste
                        }

                        // Ajouter un OnItemClickListener pour ouvrir l'activité de détails
                        listView.setOnItemClickListener((parent, view1, position, id) -> {
                            Reservation selectedReservation = reservations.get(position);

                            // Passer l'ID de la réservation à l'activité de détails
                            Intent intent = new Intent(getContext(), activity_detail_reservation.class);
                            intent.putExtra("reservation_id", selectedReservation.getId());
                            startActivity(intent);
                        });
                    }
                });
            }
        }).start();

        return view;
    }
}