package com.esprit.eventmanagement;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import entities.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.eventTitle.setText(event.getTitle());
        holder.eventDescription.setText(event.getDescription());
        holder.eventStartDate.setText("Start Date: " + event.getStartDate());
        holder.eventEndDate.setText("End Date: " + event.getEndDate());  // Binding end date
        holder.eventTicketType.setText("Ticket Type: " + event.getTicketType());  // Binding ticket type

        if (event.getImagePath() != null) {
            holder.eventImage.setImageURI(Uri.parse(event.getImagePath()));
        }
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventTitle, eventDescription, eventStartDate, eventEndDate, eventTicketType;
        ImageView eventImage;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.event_title);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventStartDate = itemView.findViewById(R.id.event_start_date);
            eventEndDate = itemView.findViewById(R.id.event_end_date);
            eventTicketType = itemView.findViewById(R.id.event_ticket_type);
            eventImage = itemView.findViewById(R.id.event_image);
        }
    }

}
