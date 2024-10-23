package DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entities.Event;

@Dao
public interface EventDAO {
    @Insert
    void addEvent(Event event);

    @Update
    void updateEvent(Event event);

    @Delete
    void deleteEvent(Event event);

    @Query("SELECT * FROM event")
    List<Event> getAllEvents();

    @Query("SELECT * FROM event WHERE id = :id")
    Event getEventById(int id);
}
