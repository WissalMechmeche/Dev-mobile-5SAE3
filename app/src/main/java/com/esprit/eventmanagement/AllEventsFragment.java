package com.esprit.eventmanagement;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import database.AppDataBase;
import entities.Event;

public class AllEventsFragment extends Fragment {
    private RecyclerView recyclerViewEvents;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_events, container, false);

        // Initialize RecyclerView
        recyclerViewEvents = view.findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch data from the database in a background thread
        fetchDataFromDatabase();

        return view;
    }

    private void fetchDataFromDatabase() {
        executorService.execute(() -> {
            // Fetch events from database
            AppDataBase db = AppDataBase.getAppDataBase(getContext());
            eventList = db.eventDAO().getAllEvents();
            Log.d("AllEventsFragment", "Fetched events: " + eventList.size());

            // Update the RecyclerView on the main thread
            getActivity().runOnUiThread(() -> {
                if (eventList != null && !eventList.isEmpty()) {
                    eventAdapter = new EventAdapter(eventList);
                    recyclerViewEvents.setAdapter(eventAdapter);
                } else {
                    Log.d("AllEventsFragment", "No events found.");
                }
            });
        });
    }
}
