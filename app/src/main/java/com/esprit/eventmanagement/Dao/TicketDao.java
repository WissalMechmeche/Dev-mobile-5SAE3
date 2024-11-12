package com.esprit.eventmanagement.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.esprit.eventmanagement.Entities.Ticket;

import java.util.List;

@Dao
public interface TicketDao {

    @Insert
    void insert(Ticket ticket);

    @Query("SELECT * FROM tickets")
    List<Ticket> getAllTickets();

    @Update
    void update(Ticket ticket);
}