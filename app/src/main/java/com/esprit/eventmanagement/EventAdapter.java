package com.esprit.eventmanagement;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.esprit.eventmanagement.entities.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private Context context;


    public EventAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventTitle.setText(event.getTitle());
        holder.eventDescription.setText(event.getDescription());
        holder.eventStartDate.setText("Start Date: " + event.getStartDate());
        holder.eventEndDate.setText("End Date: " + event.getEndDate());
        holder.eventTicketType.setText("Ticket Type: " + event.getTicketType());

        // Set OnClickListener for the buttons
        holder.btnDetails.setOnClickListener(v -> {
            // Handle details click
        });
        holder.btnModify.setOnClickListener(v -> {
            // Handle modify click
        });

        holder.btnReservation.setOnClickListener(v -> {
            if (context != null) {
                Intent intent = new Intent(context, ReservationActivity.class);
                intent.putExtra("event_id", event.getId());
                context.startActivity(intent);
            } else {
                Log.e("EventAdapter", "Context is null!");
            }
        });



    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventTitle, eventDescription, eventStartDate, eventEndDate, eventTicketType;
        ImageButton btnDetails, btnModify, btnReservation;

        EventViewHolder(View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.event_image);
            eventTitle = itemView.findViewById(R.id.event_title);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventStartDate = itemView.findViewById(R.id.event_start_date);
            eventEndDate = itemView.findViewById(R.id.event_end_date);
            eventTicketType = itemView.findViewById(R.id.event_ticket_type);
            btnDetails = itemView.findViewById(R.id.btn_details);
            btnModify = itemView.findViewById(R.id.btn_modify);
            btnReservation = itemView.findViewById(R.id.btn_reserv);
        }

    }
}