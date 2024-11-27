package com.esprit.eventmanagement.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.esprit.eventmanagement.entities.Ticket;

import java.util.List;

@Dao
public interface TicketDao {
    @Insert
    void insert(Ticket ticket);

    @Query("SELECT * FROM tickets")
    List<Ticket> getAllTickets();

    @Query("SELECT * FROM tickets WHERE id = :id")
    Ticket getTicketById(int id);

    @Update
    void update(Ticket ticket);

    @Query("DELETE FROM tickets WHERE id = :id")
    void deleteById(int id); // This method deletes the ticket by ID
}
