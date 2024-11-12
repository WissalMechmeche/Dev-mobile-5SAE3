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

        recyclerViewEvents = view.findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchDataFromDatabase();

        return view;
    }

    private void fetchDataFromDatabase() {
        // Run the database query in the background thread
        executorService.execute(() -> {
            AppDataBase db = AppDataBase.getAppDataBase(getActivity().getApplicationContext());
            if (db != null) {
                // Fetch the event list in the background
                List<Event> eventList = db.eventDAO().getAllEvents();
                if (eventList != null && !eventList.isEmpty()) {
                    // Update the RecyclerView on the UI thread
                    getActivity().runOnUiThread(() -> {
                        eventAdapter = new EventAdapter(eventList);
                        recyclerViewEvents.setAdapter(eventAdapter);
                    });
                } else {
                    Log.d("AllEventsFragment", "No events found or eventList is null.");
                }
            } else {
                Log.d("AllEventsFragment", "Database instance is null.");
            }
        });
    }



}
