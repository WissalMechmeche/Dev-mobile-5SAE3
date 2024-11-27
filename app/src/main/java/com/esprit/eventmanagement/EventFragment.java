package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.esprit.eventmanagement.database.AppDataBase;
import com.esprit.eventmanagement.entities.Event;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EventFragment extends Fragment {
    private RecyclerView recyclerViewEvents;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        // Initialize RecyclerView
        recyclerViewEvents = view.findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load events from the database
        fetchDataFromDatabase();

        // Initialize and set the button's onClickListener
        Button buttonAddEvent = view.findViewById(R.id.buttonAddEvent);
        buttonAddEvent.setOnClickListener(v -> {
            // Open the AddEventActivity
            Intent intent = new Intent(getActivity(), AddEventActivity.class);
            startActivity(intent);
        });

        Button buttonAddBillet = view.findViewById(R.id.buttonAddBillet);
        buttonAddBillet.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListProductsActivity.class); // Change AddBilletActivity to your actual billet activity
            startActivity(intent);
        });

        return view;
    }

    private void fetchDataFromDatabase() {
        executorService.execute(() -> {
            AppDataBase db = AppDataBase.getAppDataBase(getActivity().getApplicationContext());
            if (db != null) {
                eventList = db.EventDAO().getAllEvents();

                // Vérification du contenu des événements récupérés
                Log.d("AllEventsFragment", "Nombre d'événements récupérés: " + eventList.size());
                Log.d("AllEventsFragment", "Liste des événements: " + eventList.toString());

                getActivity().runOnUiThread(() -> {
                    if (eventAdapter == null) {
                        EventAdapter eventAdapter = new EventAdapter(eventList, getContext()); // Utilisez getContext() pour obtenir le contexte du fragment
                        recyclerViewEvents.setAdapter(eventAdapter);

                    } else {
                        eventAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                Log.d("AllEventsFragment", "Database instance is null.");
            }
        });
    }


}