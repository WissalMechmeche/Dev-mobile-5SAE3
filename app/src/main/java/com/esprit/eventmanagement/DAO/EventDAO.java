package com.esprit.eventmanagement.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.esprit.eventmanagement.entities.Event;

import java.util.List;

@Dao
public interface EventDAO {
    @Insert
    void addEvent(Event event);
    @Query("SELECT * FROM event")
    List<Event> getAllEvents();

    @Query("SELECT * FROM event WHERE id = :id")
    Event getEventById(int id);
    @Update
    void updateEvent(Event event);

    @Delete
    void deleteEvent(Event event);
}
