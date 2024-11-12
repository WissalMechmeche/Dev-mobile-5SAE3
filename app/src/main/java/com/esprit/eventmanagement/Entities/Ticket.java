package com.esprit.eventmanagement.Entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "tickets")


public class Ticket {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String eventName;
    private int quantity;
    private String ticketType;

    // Constructor, getters, and setters
    public Ticket(String eventName, int quantity, String ticketType) {
        this.eventName = eventName;
        this.quantity = quantity;
        this.ticketType = ticketType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }
}
